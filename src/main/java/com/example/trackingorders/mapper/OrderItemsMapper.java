package com.example.trackingorders.mapper;

import com.example.trackingorders.dto.response.OrderItemResponse;
import com.example.trackingorders.entity.CartItems;
import com.example.trackingorders.entity.OrderItems;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductsMapper.class})
public interface OrderItemsMapper {
    @Mapping(target = "price",expression = "java(cartItem.getProducts().getPrice())")
    @Mapping(target = "orders", ignore = true)
    OrderItems toOderItem(CartItems cartItem) ;
    List<OrderItems> toOrderItems(List<CartItems> cartItems);

    @Mapping(source = "products", target = "product")
    OrderItemResponse toResponse(OrderItems orderItems);
}
