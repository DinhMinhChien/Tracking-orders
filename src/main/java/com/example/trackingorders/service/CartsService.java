package com.example.trackingorders.service;

import com.example.trackingorders.dto.request.CartItemUpdateRequest;
import com.example.trackingorders.dto.response.CartItemResponse;

import java.util.List;

public interface CartsService {
    List<CartItemResponse> getMyCart() ;
    CartItemResponse updateCartItem(String cartItemId,CartItemUpdateRequest request) ;
    void deleteCartItem(String id) ;
}
