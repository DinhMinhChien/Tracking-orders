package com.example.trackingorders.service.implement;

import com.example.trackingorders.common.StatusOrderEnum;
import com.example.trackingorders.common.StatusReturnEnum;
import com.example.trackingorders.dto.ReturnOrderExportDTO;
import com.example.trackingorders.dto.request.ReturnsRequest;
import com.example.trackingorders.dto.response.DetailReturnResponse;
import com.example.trackingorders.dto.response.ReturnSummaryProjection;
import com.example.trackingorders.dto.response.ReturnSummaryResponse;
import com.example.trackingorders.dto.response.ReturnsResponse;
import com.example.trackingorders.entity.OrderItems;
import com.example.trackingorders.entity.Orders;
import com.example.trackingorders.entity.Products;
import com.example.trackingorders.entity.Returns;
import com.example.trackingorders.exception.BusinessException;
import com.example.trackingorders.mapper.OrdersMapper;
import com.example.trackingorders.mapper.ReturnMapper;
import com.example.trackingorders.repository.OrdersRepository;
import com.example.trackingorders.repository.ReturnsRepository;
import com.example.trackingorders.service.InventoryService;
import com.example.trackingorders.service.PromotionService;
import com.example.trackingorders.service.ReturnsService;
import com.example.trackingorders.service.TrackingLogsService;
import com.example.trackingorders.service.specfication.OrderSpecification;
import com.example.trackingorders.service.specfication.ReturnsSpecification;
import com.example.trackingorders.util.ProductQuantityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ReturnsServiceImplement implements ReturnsService {
    private final ReturnsRepository returnsRepository ;
    private final OrdersRepository ordersRepository ;
    private final OrdersMapper ordersMapper ;
    private final ReturnMapper returnMapper ;
    private final TrackingLogsService trackingLogsService;
    private final InventoryService inventoryService ;
    private final PromotionService promotionService ;

    @Override
    public ReturnSummaryResponse getSummary() {
        LocalDate today = LocalDate.now() ;
        int startMonthOfQuarter = ((today.getMonthValue() - 1) / 3) * 3 + 1 ;
        LocalDateTime startOfQuarter = LocalDate.of(today.getYear(), startMonthOfQuarter, 1).atStartOfDay() ;
        List<String> activeStatuses = List.of(
                StatusReturnEnum.PENDING.name(),
                StatusReturnEnum.IN_TRANSIT.name(),
                StatusReturnEnum.WAREHOUSE_RECEIVED.name(),
                StatusReturnEnum.RESTOCKED.name()
        ) ;
        ReturnSummaryProjection stats = returnsRepository.getDashboardStats(
                activeStatuses,
                StatusReturnEnum.WAREHOUSE_RECEIVED.name(),
                StatusReturnEnum.REFUNDED.name(),
                startOfQuarter
        ) ;
        return new ReturnSummaryResponse(
                stats.getActiveReturns(),
                stats.getAwaitingInspection(),
                stats.getTotalRefunds()
        ) ;
    }

    @Override
    public Page<ReturnsResponse> getAll(StatusReturnEnum status,Integer pageNumber,Integer pageSize) {
        if (pageNumber == null || pageNumber < 0) {
            pageNumber = 0;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }

        Pageable pageable = PageRequest.of(pageNumber,pageSize) ;
        Specification<Returns> specification = Specification.where(ReturnsSpecification.fetchListRelations()) ;
        if (status != null ) {
            specification = specification.and(ReturnsSpecification.likeStatus(status)) ;
        }
        Page<Returns> returns = returnsRepository.findAll(specification,pageable) ;
        Page<ReturnsResponse> responses = returns.map(returnMapper::toResponse) ;
        return responses ;
    }

    @Override
    public ReturnsResponse create(ReturnsRequest request) {

        Optional<Orders> ordersOptional = ordersRepository.findWithUserById(request.getOrderId());
        if (ordersOptional.isEmpty()) {
            throw new  BusinessException("Not exist order !") ;
        }

        Orders orders = ordersOptional.get() ;
        if (orders.getStatus() != StatusOrderEnum.DELIVERED) {
            throw new BusinessException("Only delivered orders can be returned");
        }
        Returns returns = new Returns() ;
        returns.setOrders(orders);
        returns.setStatus(StatusReturnEnum.PENDING);
        returns.setReason(request.getReason());
        returns.setReceivedAt(LocalDateTime.now());
        returns.setOriginType(request.getOriginType());

        Returns returnSave = returnsRepository.save(returns);
        ReturnsResponse response = new ReturnsResponse() ;
        response.setId(returnSave.getId());
        response.setCustomer(orders.getUsers().getFullName());
        response.setReason(returnSave.getReason());
        response.setOriginType(returnSave.getOriginType());
        response.setStatus(returnSave.getStatus());
        return response ;
    }

    public DetailReturnResponse getDetail(String returnId) {
        if (returnId == null || returnId.isEmpty()) {
            throw new BusinessException("Error! Field returnId is null") ;
        }
        Optional<Returns> returnsOptional = returnsRepository.findWithOrderAndUserById(returnId);
        if (returnsOptional.isEmpty()) {
            throw new BusinessException("Not found return order") ;
        }
        Returns returns = returnsOptional.get();

        DetailReturnResponse response = new DetailReturnResponse() ;

        response.setOrders(ordersMapper.toResponse(returns.getOrders()));
        response.setReason(returns.getReason());
        response.setReceivedAt(returns.getReceivedAt());
        response.setOriginType(returns.getOriginType());

        return response ;
    }

    @Override
    public List<ReturnOrderExportDTO> getDataForExport(StatusReturnEnum status) {
        List<Returns> returns = returnsRepository.findAllForExport(status);
        List<ReturnOrderExportDTO> returnExports = returns.stream().map(returns1 -> ReturnOrderExportDTO.builder()
                .returnId(returns1.getId())
                .customerInfo(getCustomerInfo(returns1))
                .reason(returns1.getReason())
                .originType(returns1.getOriginType() == null ? "" : returns1.getOriginType().name())
                .status(returns1.getStatus() == null ? "" : returns1.getStatus().name()).build()).toList();
        return returnExports;
    }

    private String getCustomerInfo(Returns returns) {
        if (returns.getOrders() == null) {
            return "";
        }
        String fullName = returns.getOrders().getUsers() == null ? "" : returns.getOrders().getUsers().getFullName();
        return fullName + " / " + returns.getOrders().getId();
    }

    private void validateStatusTransition(StatusReturnEnum currentStatus, StatusReturnEnum newStatus) {
        if (!isValidTransition(currentStatus, newStatus)) {
            throw new BusinessException("Invalid order status transition") ;
        }
    }

    private boolean isValidTransition(StatusReturnEnum currentStatus, StatusReturnEnum newStatus) {
        if (currentStatus == null || newStatus == null) {
            return false ;
        }
        return switch (currentStatus) {
            case PENDING ->
                    newStatus == StatusReturnEnum.IN_TRANSIT ||
                    newStatus == StatusReturnEnum.FAILED ;
            case IN_TRANSIT ->
                    newStatus == StatusReturnEnum.WAREHOUSE_RECEIVED ||
                    newStatus == StatusReturnEnum.FAILED ;
            case WAREHOUSE_RECEIVED ->
                    newStatus == StatusReturnEnum.RESTOCKED ||
                    newStatus == StatusReturnEnum.FAILED ;
            case RESTOCKED ->
                    newStatus == StatusReturnEnum.REFUNDED;
            case REFUNDED, REJECTED, FAILED -> false;
        };
    }
    @Override
    public void confirm(String returnId, StatusReturnEnum status) {
        status = StatusReturnEnum.IN_TRANSIT ;
        if (returnId == null) {
            throw new BusinessException("Field returnId is null") ;
        }
        
        Optional<Returns> returnsOptional = returnsRepository.findWithOrderAndUserById(returnId) ;
        if (returnsOptional.isEmpty()) {
            throw new BusinessException("Not found returns order") ;
        }

        Returns returns = returnsOptional.get() ;
        StatusReturnEnum currentStatus = returns.getStatus() ;
        validateStatusTransition(currentStatus,status) ;
        returns.setStatus(status);
        returnsRepository.save(returns) ;
        trackingLogsService.createLog(
                returns.getOrders().getId(),
                currentStatus.toString(),status.toString(),
                "Xác nhận đơn hoàn trả thành công . Đơn hoàn trả đang tiếp tục được xử lí",
                "Hệ thống"
        );

    }

    @Override
    public void reject(String returnId, StatusReturnEnum status) {
        status = StatusReturnEnum.REJECTED ;
        if (returnId == null) {
            throw new BusinessException("Field returnId is null") ;
        }
        Optional<Returns> returnsOptional = returnsRepository.findWithOrderAndUserById(returnId) ;
        if (returnsOptional.isEmpty()) {
            throw new BusinessException("Not found return order") ;
        }
        Returns returns = returnsOptional.get() ;
        StatusReturnEnum currentStatus = returns.getStatus() ;
        validateStatusTransition(currentStatus,status);
        returns.setStatus(status);
        returnsRepository.save(returns) ;
        trackingLogsService.createLog(
                returns.getOrders().getId(),
                currentStatus.toString(),
                status.toString(),
                "Từ chối hoàn trả đơn hàng do đơn hàng ko đạt yêu cầu ",
                "Hệ thống"
        );
    }

    @Override
    public void returnsSuccess(String returnId) {
        if (returnId == null ) {
            throw new BusinessException("Field id is null") ;
        }
        Optional<Returns> returnsOptional = returnsRepository.findWithOrderItemsAndProductAndPromotion(returnId) ;
        if (returnsOptional.isEmpty()) {
            throw new BusinessException("Not found return order") ;
        }
        Returns returns = returnsOptional.get();
        StatusReturnEnum currentStatus = returns.getStatus() ;
        validateStatusTransition(currentStatus,StatusReturnEnum.REFUNDED);
        List<OrderItems> orderItems = returns.getOrders().getOrderItems();
        List<Products> products = orderItems.stream().map(OrderItems::getProducts).toList() ;
        List<String> productIds = products.stream().map(Products::getId).toList();
        List<Integer> quantities = orderItems.stream().map(OrderItems::getQuantity).toList() ;
        Map<String,Integer> quantityByProductId = ProductQuantityUtils.toQuantityByProductId(productIds,quantities);
        inventoryService.restoreQuantityProduct(products,quantityByProductId);
        promotionService.restorePromotion(returns.getOrders().getPromotions());

        returns.setStatus(StatusReturnEnum.REFUNDED);
        returnsRepository.save(returns);
        trackingLogsService.createLog(
                returns.getOrders().getId(),
                currentStatus.toString(),
                StatusReturnEnum.REFUNDED.toString(),
                "Return order success",
                "Hệ thống"
        );

    }
}
