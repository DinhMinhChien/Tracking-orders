package com.example.trackingorders.service.implement;

import com.example.trackingorders.common.StatusReturnEnum;
import com.example.trackingorders.dto.ReturnOrderExportDTO;
import com.example.trackingorders.dto.request.ReturnsRequest;
import com.example.trackingorders.dto.response.DetailReturnResponse;
import com.example.trackingorders.dto.response.ReturnSummaryResponse;
import com.example.trackingorders.dto.response.ReturnsResponse;
import com.example.trackingorders.entity.Orders;
import com.example.trackingorders.entity.Returns;
import com.example.trackingorders.exception.BusinessException;
import com.example.trackingorders.mapper.OrdersMapper;
import com.example.trackingorders.mapper.ReturnMapper;
import com.example.trackingorders.repository.OrdersRepository;
import com.example.trackingorders.repository.ReturnsRepository;
import com.example.trackingorders.service.ReturnsService;
import com.example.trackingorders.service.specfication.ReturnsSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReturnsServiceImplement implements ReturnsService {
    private final ReturnsRepository returnsRepository ;
    private final OrdersRepository ordersRepository ;
    private final OrdersMapper ordersMapper ;
    private final ReturnMapper returnMapper ;
    @Override
    public ReturnSummaryResponse getSummary() {
        ReturnSummaryResponse response = returnsRepository.getDashboardStats() ;
        return response ;
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
        Specification specification = Specification.where(null) ;
        if (status != null ) {
            specification = specification.and(ReturnsSpecification.likeStatus(status)) ;
        }
        Page<Returns> returns = returnsRepository.findAll(specification,pageable) ;
        Page<ReturnsResponse> responses = returns.map(entity -> returnMapper.toResponse(entity)) ;
        return responses ;
    }

    @Override
    public ReturnsResponse create(ReturnsRequest request) {
        Optional<Orders> ordersOptional = ordersRepository.findById(request.getOrderId());
        if (ordersOptional.isEmpty()) {
            throw new  BusinessException("Không tồn tại đơn hàng") ;
        }
        Orders orders = ordersOptional.get() ;
        Returns returns = new Returns() ;
        returns.setOrders(orders);
        returns.setStatus(StatusReturnEnum.PENDING_APPROVAL);
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
        Optional<Returns> returnsOptional = returnsRepository.findById(returnId);
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
        List<Returns> returns = (status == null)
                ? returnsRepository.findByDeletedFalse()
                : returnsRepository.findByStatusAndDeletedFalse(status);
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
}
