package com.example.trackingorders.service.implement;

import com.example.trackingorders.common.StatusProductsEnum;
import com.example.trackingorders.dto.request.CartItemUpdateRequest;
import com.example.trackingorders.dto.response.CartItemResponse;
import com.example.trackingorders.entity.CartItems;
import com.example.trackingorders.entity.Inventory;
import com.example.trackingorders.entity.Products;
import com.example.trackingorders.mapper.ProductsMapper;
import com.example.trackingorders.repository.CartItemsRepository;
import com.example.trackingorders.repository.CartsRepository;
import com.example.trackingorders.repository.InventoryRepository;
import com.example.trackingorders.repository.ProductsRepository;
import com.example.trackingorders.service.CartsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartsServiceImplement implements CartsService {

    private final CartItemsRepository cartItemsRepository ;
    private final ProductsRepository productsRepository ;
    private final ProductsMapper productsMapper ;
    private final InventoryRepository inventoryRepository ;

    @Override
    public List<CartItemResponse> getMyCart() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName() ;

        List<CartItems> cartItems = cartItemsRepository.findCartItemsByUsername(username) ;
        List<Products> products = cartItems.stream().map(CartItems::getProducts).toList() ;
        List<Inventory> inventories = inventoryRepository.findInventoriesByProducts(products) ;

        List<CartItemResponse> cartItemResponses = new ArrayList<>() ;

        for(int i = 0; i < cartItems.size(); i++) {
            CartItemResponse cartItemResponse = new CartItemResponse() ;

            cartItemResponse.setId(cartItems.get(i).getId());
            cartItemResponse.setQuantity(cartItems.get(i).getQuantity());

            cartItemResponse.setProduct(productsMapper.toProductResponse(cartItems.get(i).getProducts(),inventories.get(i)));

            if (cartItems.get(i).getQuantity() <= inventories.get(i).getQuantity() && inventories.get(i).getQuantity() > 10) {
                cartItemResponse.setStatus(StatusProductsEnum.IN_STOCK);
            }
            if (cartItems.get(i).getQuantity() <= inventories.get(i).getQuantity() && inventories.get(i).getQuantity() <=10 ) {
                cartItemResponse.setStatus(StatusProductsEnum.LIMITED_STOCK);
            }
            if(cartItems.get(i).getQuantity() > inventories.get(i).getQuantity()) {
                cartItemResponse.setStatus(StatusProductsEnum.OUT_OF_STOCK);
            }
            cartItemResponses.add(cartItemResponse) ;
        }

        return cartItemResponses;
    }

    @Override
    public CartItemResponse updateCartItem(String cartItemId,CartItemUpdateRequest request) {

        Optional<CartItems> cartItemsOptional = cartItemsRepository.findById(cartItemId) ;
        CartItems cartItem = cartItemsOptional.get() ;
        Integer quantityRequest = request.getQuantity() ;
        Integer quantity = productsRepository.findQuantity(cartItem.getProducts()) ;

        CartItemResponse cartItemResponse = new CartItemResponse() ;
        cartItemResponse.setId(cartItemId);
        Inventory inventory = inventoryRepository.findInventoriesByProducts(cartItem.getProducts()) ;
        cartItemResponse.setProduct(productsMapper.toProductResponse(cartItem.getProducts(),inventory));
        if (quantityRequest <= quantity ) {
            cartItem.setQuantity(quantityRequest);
            if (quantity <= 10) {
                cartItemResponse.setStatus(StatusProductsEnum.LIMITED_STOCK);
            } else {
                cartItemResponse.setStatus(StatusProductsEnum.IN_STOCK);
            }

            cartItemsRepository.save(cartItem) ;
        } else {
            cartItemResponse.setStatus(StatusProductsEnum.OUT_OF_STOCK);
        }
        cartItemResponse.setQuantity(cartItem.getQuantity());
        return cartItemResponse;
    }

    @Transactional
    @Override
    public Void deleteCartItem(String id) {
        Optional<CartItems> cartItems = cartItemsRepository.findById(id) ;
        cartItems.get().setDeleted(true);
        cartItemsRepository.save(cartItems.get()) ;
        return null;
    }

}
