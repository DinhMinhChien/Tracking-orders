package com.example.trackingorders.service.implement;

import com.example.trackingorders.common.StatusOrderEnum;
import com.example.trackingorders.dto.request.OrdersRequest;
import com.example.trackingorders.dto.response.*;
import com.example.trackingorders.entity.*;
import com.example.trackingorders.exception.BusinessException;
import com.example.trackingorders.mapper.*;
import com.example.trackingorders.repository.*;
import com.example.trackingorders.service.CheckoutService;
import com.example.trackingorders.service.OrdersService;
import com.example.trackingorders.service.specfication.OrderSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
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
    private final InventoryRepository inventoryRepository ;
    private final OrderItemsMapper orderItemsMapper;
    private final TrackingLogsRepository trackingLogsRepository ;
    private final ProductsRepository productsRepository ;

    @Override
    public OrdersResponse create(OrdersRequest request) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName() ;
        List<String> productIds = request.getProductIds() ;
        List<Integer> quantities = request.getQuantities();
        String promotionId = request.getPromotionId() ;
        String addressId = request.getAddressId() ;
        Boolean isFromCart = request.getIsFromCart();

        Users user = usersRepository.findByUsername(username) ;
        PaymentMethods paymentMethod = paymentMethodsRepository.findPaymentMethodsByType("COD") ;
        Promotions promotion = promotionsRepository.findById(promotionId).get();
        List<Carriers> carriers = carriersRepository.findAll();
        Carriers radomCarrier = carriers.get(new Random().nextInt(carriers.size())) ;
        List<Users> shippersInCarrier = usersRepository.findByCarrier(radomCarrier) ;
        Users radomShipper = shippersInCarrier.get(new Random().nextInt(shippersInCarrier.size())) ;
        String shippingAddress = addressesRepository.findById(addressId).get().getFullAddress() ;


        CheckoutSummaryResponse checkout = checkoutService.getSummary(ordersMapper.toCheckout(request)) ;

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

        //thêm vào orderItems
        for(int i = 0 ;i < products.size();i++) {
            OrderItems orderItem = new OrderItems() ;

            orderItem.setOrders(order);
            orderItem.setProducts(products.get(i));
            orderItem.setQuantity(quantities.get(i));
            orderItem.setPrice(products.get(i).getPrice());

            orderItems.add(orderItem) ;
        }

        orderItemsRepository.saveAll(orderItems) ;

        //thêm log cho đơn hàng này ở trạng thái PENDING
        TrackingLogs log = new TrackingLogs() ;
        log.setOrderId(order.getId());
        log.setToStatus(StatusOrderEnum.PENDING);
        log.setNote("Đơn hàng đang chờ xác nhận");
        trackingLogsRepository.save(log) ;


        // Xoá các cartItem ra cart sau khi đã đặt hàng
//        for (CartItems items : cartItems) {
//            items.setDeleted(true);
//            products.add(items.getProducts()) ;
//            quantities.add(items.getQuantity()) ;
//        }
//        cartItemsRepository.saveAll(cartItems) ;

        // Giảm quantity của product sau khi đã đặt hàng
        for (int i = 0;i < products.size();i++) {
            inventoryRepository.decreaseStockQuantity(products.get(i),quantities.get(i));
        }

        // Giảm sô lượt sử dụng của voucher
        //todo sử dụng updatedAt để xử lí tranh chấp nếu 2 người cùng dung voucher này
        promotionsRepository.decreaseUsagesLimit(promotionId);

        OrdersResponse response = ordersMapper.toResponse(order) ;
        return response;
    }

    @Override
    public OrdersResponse getDetail(String id) {
        if (id == null || id.isEmpty()) {
            throw new BusinessException("Field id is null") ;
        }
        Optional<Orders> ordersOptional = ordersRepository.findById(id) ;
        if (ordersOptional == null || ordersOptional.isEmpty()) {
            throw new BusinessException("Not found order") ;
        }
        OrdersResponse response = ordersMapper.toResponse(ordersOptional.get()) ;
        return response ;
    }

    @Override
    public OrderDashboardStats getHeaderStats() {
        OrderDashboardStats response = ordersRepository.getDashboardStatistics() ;
        return response ;
    }

    @Override
    public Page<OrdersResponse> getAll(int pageNumber, int pageSize, StatusOrderEnum status) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize) ;
        Specification specification = Specification.where(null) ;
        if (status != null) {
            specification = specification.and(OrderSpecification.likeStatus(status)) ;
        }
        Page<Orders> orders = ordersRepository.findAll(specification,pageable) ;
        Page<OrdersResponse> responses = orders.map(entity -> ordersMapper.toResponse(entity)) ;
        return responses;
    }

    @Override
    public void bulkConfirm(List<String> orderIds) {
        StatusOrderEnum oldStatus = StatusOrderEnum.PENDING ;
        StatusOrderEnum newStatus = StatusOrderEnum.CONFIRMED ;
        List<Orders> orderConfirms = ordersRepository.findALLByIdAndStatus(orderIds, oldStatus) ;
        orderConfirms.forEach(order -> {
            order.setStatus(newStatus);
            TrackingLogs log = new TrackingLogs();
            log.setOrderId(order.getId());
            log.setFromStatus(oldStatus);
            log.setToStatus(newStatus);
            log.setNote("Đơn hàng đã được xác nhận bởi Admin (Bulk Action)");
            log.setLocation("Hệ thống quản lý");

            trackingLogsRepository.save(log);
        });
        ordersRepository.saveAll(orderConfirms);
    }

    @Override
    public void confirmPickUp(String id, StatusOrderEnum status) {
        if (id == null) {
            throw new BusinessException("Field id is null") ;
        }
        Optional<Orders> ordersOptional = ordersRepository.findById(id) ;
        if (ordersOptional == null ) {
            throw new BusinessException("Order not exist") ;
        }
        if (status == null) {
            throw new BusinessException("Field status is null") ;
        }
        Orders order = ordersOptional.get();
        StatusOrderEnum oldStatus = order.getStatus() ;
        order.setStatus(status);
        ordersRepository.save(order) ;
        TrackingLogs logs = new TrackingLogs() ;
        logs.setOrderId(order.getId());
        logs.setFromStatus(oldStatus);
        logs.setToStatus(status);
        logs.setNote("Lấy hàng thành công");
        logs.setLocation("Nhà kho");
        trackingLogsRepository.save(logs) ;

    }

    @Override
    public void confirmDeliverySuccess(String id, StatusOrderEnum status) {
        if (id == null) {
            throw new BusinessException("Field id is null") ;
        }
        Optional<Orders> ordersOptional = ordersRepository.findById(id) ;
        if (ordersOptional == null || ordersOptional.isEmpty()) {
            throw new BusinessException("Order not exist") ;
        }
        Orders order = ordersOptional.get() ;
        StatusOrderEnum oldStatus = order.getStatus() ;
        order.setStatus(status);
        TrackingLogs log = new TrackingLogs() ;
        log.setLocation(order.getShippingAddress());
        log.setOrderId(order.getId());
        log.setFromStatus(oldStatus);
        log.setToStatus(status);
        log.setNote("Giao hàng thành công");
        ordersRepository.save(order);
        trackingLogsRepository.save(log) ;
    }
}
