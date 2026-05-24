package com.example.trackingorders.service.implement;

import com.example.trackingorders.common.StatusOrderEnum;
import com.example.trackingorders.dto.request.OrdersRequest;
import com.example.trackingorders.dto.response.*;
import com.example.trackingorders.entity.*;
import com.example.trackingorders.exception.BusinessException;
import com.example.trackingorders.mapper.*;
import com.example.trackingorders.repository.*;
import com.example.trackingorders.service.*;
import com.example.trackingorders.service.specfication.OrderSpecification;
import com.example.trackingorders.util.ProductQuantityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class OrdersServiceImplement implements OrdersService {
    private final CheckoutService checkoutService ;
    private final OrdersMapper ordersMapper ;
    private final UsersRepository usersRepository ;
    private final PaymentMethodsRepository paymentMethodsRepository ;
    private final PromotionsRepository promotionsRepository ;
    private final CarriersRepository carriersRepository ;
    private final AddressesRepository addressesRepository ;
    private final OrdersRepository ordersRepository ;
    private final CartItemsRepository cartItemsRepository ;
    private final OrderItemsRepository orderItemsRepository ;
    private final ProductsRepository productsRepository ;
    private final InventoryService inventoryService ;
    private final TrackingLogsService trackingLogsService ;
    private final PromotionService promotionService;

    @Override
    public OrderDetailResponse create(OrdersRequest request) {

        //Lấy dữ liệu từ request
        String username = SecurityContextHolder.getContext().getAuthentication().getName() ;

        String promotionId = request.getPromotionId() ;
        String addressId = request.getAddressId() ;
        Boolean isFromCart = request.getIsFromCart();

        List<String> productIds = request.getProductIds() ;
        List<Integer> quantities = request.getQuantities();
        Map<String, Integer> quantityByProductId = ProductQuantityUtils.toQuantityByProductId(productIds, quantities) ;

        //Tạo các thuộc tính gắn vào đơn hàng
        Users user = usersRepository.findByUsername(username) ;
        PaymentMethods paymentMethod = paymentMethodsRepository.findPaymentMethodsByType("COD") ;
        Promotions promotion = promotionsRepository.findById(promotionId).get();
        List<Carriers> carriers = carriersRepository.findAll();
        Carriers radomCarrier = carriers.get(new Random().nextInt(carriers.size())) ;
        List<Users> shippersInCarrier = usersRepository.findByCarrier(radomCarrier) ;
        Users radomShipper = shippersInCarrier.get(new Random().nextInt(shippersInCarrier.size())) ;
        String shippingAddress = addressesRepository.findById(addressId).get().getFullAddress() ;


        CheckoutSummaryResponse checkout = checkoutService.getSummary(ordersMapper.toCheckout(request)) ;

        //set các thuộc tính cho đơn hàng
        Orders order = new Orders() ;
        order.setUsers(user);
        order.setPaymentMethod(paymentMethod);
        order.setPromotions(promotion);
        order.setCarriers(radomCarrier);
        order.setShipper(radomShipper);
        order.setTotalPrice(checkout.getSubTotal());
        order.setShippingFee(checkout.getExpeditedShipping());
        order.setShippingAddress(shippingAddress);
        order.setStatus(StatusOrderEnum.PENDING);

        ordersRepository.save(order) ;

        List<OrderItems> orderItems = new ArrayList<>() ;
        List<Products> products = productsRepository.findAllById(productIds) ;

        ProductQuantityUtils.validateAllProductsFound(products, quantityByProductId) ;

        //thêm vào orderItems
        for(Products product : products) {
            OrderItems orderItem = new OrderItems() ;

            orderItem.setOrders(order);
            orderItem.setProducts(product);
            orderItem.setQuantity(quantityByProductId.get(product.getId()));
            orderItem.setPrice(product.getPrice());

            orderItems.add(orderItem) ;
        }
        orderItemsRepository.saveAll(orderItems) ;

        //Thêm log cho đơn hàng này ở trạng thái PENDING
        trackingLogsService.createLog(
                order.getId(),
                null,
                StatusOrderEnum.PENDING.toString(),
                "Đơn hàng đang chờ xác nhận",
                null
        ) ;

        //xoá sản phẩm khỏi giỏ hàng nếu đơn tạo từ giỏ hàng
        if (isFromCart == true) {
            cartItemsRepository.removeCartItemsByProductIdsAndUserId(productIds, user.getId()) ;
        }

        // Giảm quantity của product sau khi đã đặt hàng
        inventoryService.decreaseStock(products,quantityByProductId);

        // Giảm sô lượt sử dụng của voucher
        promotionService.usePromotion(user,promotion,order);

        OrderDetailResponse response = ordersMapper.toResponse(order) ;
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDetailResponse getDetail(String id) {

        Orders order = findOrderById(id, "Field id is null", "Not found order") ;
        OrderDetailResponse response = ordersMapper.toResponse(order) ;
        return response ;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDashboardStats getHeaderStats() {
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay() ;

        return new OrderDashboardStats(
                ordersRepository.sumMonthToDateRevenue(startOfMonth, StatusOrderEnum.FAILED),
                ordersRepository.countByDeletedFalse(),
                ordersRepository.countByStatusAndDeletedFalse(StatusOrderEnum.PENDING),
                ordersRepository.countByStatusAndDeletedFalse(StatusOrderEnum.SHIPPING),
                ordersRepository.countByStatusAndDeletedFalse(StatusOrderEnum.FAILED)
        ) ;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderListResponse> getAll(int pageNumber, int pageSize, StatusOrderEnum status) {

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize) ;
        Specification<Orders> specification = Specification.where(OrderSpecification.fetchListRelations()) ;

        if (status != null) {
            specification = specification.and(OrderSpecification.likeStatus(status)) ;
        }

        Page<Orders> orders = ordersRepository.findAll(specification,pageable) ;
        Page<OrderListResponse> responses = orders.map(order -> {
            OrderListResponse response = new OrderListResponse() ;
            response.setId(order.getId());
            response.setCustomerName(order.getUsers() == null ? null : order.getUsers().getFullName());
            response.setCreatedAt(order.getCreatedAt());
            response.setPaymentMethod(order.getPaymentMethod() == null ? null : order.getPaymentMethod().getName());
            response.setTotalPrice(order.getTotalPrice());
            response.setStatus(order.getStatus());
            response.setCarrierName(order.getCarriers() == null ? null : order.getCarriers().getName());
            return response ;
        }) ;

        return responses;
    }

    @Override
    public void bulkConfirm(List<String> orderIds) {

        StatusOrderEnum oldStatus = StatusOrderEnum.PENDING ;
        StatusOrderEnum newStatus = StatusOrderEnum.CONFIRMED ;

        List<Orders> orderConfirms = ordersRepository.findAllByIdAndStatus(orderIds, oldStatus) ;
        orderConfirms.forEach(order -> {
            changeOrderStatus(order, newStatus);
        });

        List<String> confirmedOrderIds = orderConfirms.stream().map(Orders::getId).toList() ;

        trackingLogsService.createLogs(
                confirmedOrderIds,
                oldStatus.toString(),
                newStatus.toString(),
                "Đơn hàng đã được xác nhận bởi Admin (Bulk Action)",
                "Hệ thống quản lý"
        ) ;

        ordersRepository.saveAll(orderConfirms);
    }

    @Override
    public void confirmPickUp(String id, StatusOrderEnum status) {

        Orders order = findOrderById(id, "Field id is null", "Order not exist") ;
        StatusOrderEnum oldStatus = updateOrderStatus(order, status) ;

        trackingLogsService.createLog(
                order.getId(),
                oldStatus.toString(),
                status.toString(),
                "Lấy hàng thành công",
                "Nhà kho"
        ) ;

    }

    @Override
    public void confirmDeliverySuccess(String id, StatusOrderEnum status) {
        Orders order = findOrderById(id, "Field id is null", "Order not exist") ;
        StatusOrderEnum oldStatus = updateOrderStatus(order, status) ;

        trackingLogsService.createLog(
                order.getId(),
                oldStatus.toString(),
                status.toString(),
                "Giao hàng thành công",
                order.getShippingAddress()
        ) ;
    }

    private void validateStatusTransition(StatusOrderEnum currentStatus, StatusOrderEnum newStatus) {
        if (!isValidTransition(currentStatus, newStatus)) {
            throw new BusinessException("Invalid order status transition") ;
        }
    }

    private Orders findOrderById(String id, String nullMessage, String notFoundMessage) {
        if (id == null || id.isEmpty()) {
            throw new BusinessException(nullMessage) ;
        }

        return ordersRepository.findById(id)
                .orElseThrow(() -> new BusinessException(notFoundMessage)) ;
    }

    private StatusOrderEnum updateOrderStatus(Orders order, StatusOrderEnum newStatus) {
        StatusOrderEnum oldStatus = changeOrderStatus(order, newStatus) ;
        ordersRepository.save(order) ;
        return oldStatus ;
    }

    private StatusOrderEnum changeOrderStatus(Orders order, StatusOrderEnum newStatus) {
        StatusOrderEnum oldStatus = order.getStatus() ;
        validateStatusTransition(oldStatus, newStatus);
        order.setStatus(newStatus);
        return oldStatus ;
    }

    private boolean isValidTransition(StatusOrderEnum currentStatus, StatusOrderEnum newStatus) {
        if (currentStatus == null || newStatus == null) {
            return false ;
        }
        return switch (currentStatus) {
            case PENDING -> newStatus == StatusOrderEnum.CONFIRMED || newStatus == StatusOrderEnum.FAILED ;
            case CONFIRMED -> newStatus == StatusOrderEnum.PICKING || newStatus == StatusOrderEnum.FAILED ;
            case PICKING -> newStatus == StatusOrderEnum.SHIPPING || newStatus == StatusOrderEnum.FAILED ;
            case SHIPPING -> newStatus == StatusOrderEnum.DELIVERED || newStatus == StatusOrderEnum.FAILED ;
            case DELIVERED -> newStatus == StatusOrderEnum.RETURNING ;
            case FAILED, RETURNING -> false ;
        };
    }

    @Override
    public void confirmOrder(String id) {
        Orders order = findOrderById(id, "Field id is null", "Not found order") ;
        StatusOrderEnum newStatus = StatusOrderEnum.CONFIRMED ;
        StatusOrderEnum currStatus = updateOrderStatus(order, newStatus) ;

        trackingLogsService.createLog(id,currStatus.toString(),newStatus.toString(),"Đơn hàng được xác nhận ","Hệ thống quản lý ");
    }

    @Override
    public void rejectOrder(String id, String reason) {

        Orders order = findOrderById(id, "Field id is null", "Not found order") ;
        List<OrderItems> orderItems = orderItemsRepository.findWithProductsByOrderId(id) ;
        if (orderItems.isEmpty()) {
            throw new BusinessException("Order has no items") ;
        }
        List<String> productIds = orderItems.stream().map(orderItems1 -> orderItems1.getProducts().getId()).toList() ;
        List<Integer> quantities = orderItems.stream().map(OrderItems::getQuantity).toList() ;
        Map<String,Integer> quantityByProductId = ProductQuantityUtils.toQuantityByProductId(productIds,quantities);
        List<Products> products = orderItems.stream().map(OrderItems::getProducts).toList() ;


        StatusOrderEnum newStatus = StatusOrderEnum.FAILED ;
        StatusOrderEnum currStatus = changeOrderStatus(order, newStatus) ;

        inventoryService.restoreQuantityProduct(products,quantityByProductId);
        promotionService.restorePromotion(order) ;
        ordersRepository.save(order) ;
        trackingLogsService.createLog(id,currStatus.toString(),newStatus.toString(),"Đơn hàng bị từ chối ","Hệ thống quản lý ");
    }

    @Override
    public void confirmShipping(String id, StatusOrderEnum status) {
        Orders order = findOrderById(id, "Order id is null", "Order not exist") ;
        StatusOrderEnum oldStatus = updateOrderStatus(order, status) ;

        trackingLogsService.createLog(
                order.getId(),
                oldStatus.toString(),
                status.toString(),
                "Đơn hàng đang trong quá trinh vận chuyển ",
                "Nhà kho"
        ) ;

    }
}
