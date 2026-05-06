package com.example.trackingorders.controller;

import com.example.trackingorders.common.BaseResponse;
import com.example.trackingorders.dto.request.CartItemUpdateRequest;
import com.example.trackingorders.dto.response.CartItemResponse;
import com.example.trackingorders.service.CartsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/carts")
public class CartsController {

    private final CartsService cartsService ;
    private final MessageSource messageSource ;

    //http://localhost:8001/api/v1/carts
    @GetMapping
    public ResponseEntity<BaseResponse<List<CartItemResponse>>> getMyCart() {
        List<CartItemResponse> cartItemResponses = cartsService.getMyCart() ;
        String message = messageSource.getMessage(
                "LoadMyCart.message",
                null,
                LocaleContextHolder.getLocale()
        );
        return ResponseEntity.ok(BaseResponse.ofSuccess(cartItemResponses,message)) ;
    }

    //http://localhost:8001/api/v1/carts/items/{cartItemId}
    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<BaseResponse<CartItemResponse>> updateCartItem(@PathVariable String cartItemId ,@RequestBody CartItemUpdateRequest request) {
        CartItemResponse cartItems = cartsService.updateCartItem(cartItemId,request) ;
        String message = messageSource.getMessage(
                "UpdateProductOnCart.message",
                null,
                LocaleContextHolder.getLocale()
        );
        return ResponseEntity.ok(BaseResponse.ofSuccess(cartItems,message)) ;
    }

    //http://localhost:8001/api/v1/carts/items/{id}
    @DeleteMapping("/items/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteCartItem(@PathVariable String id) {
        cartsService.deleteCartItem(id) ;
        String message = messageSource.getMessage(
                "DeleteItemFromCart.message",
                null,
                LocaleContextHolder.getLocale()
        );
        return ResponseEntity.ok(BaseResponse.ofDeleteSuccess(message));
    }

}
