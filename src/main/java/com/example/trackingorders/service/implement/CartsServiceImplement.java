package com.example.trackingorders.service.implement;

import com.example.trackingorders.common.StatusProductsEnum;
import com.example.trackingorders.dto.request.CartItemUpdateRequest;
import com.example.trackingorders.dto.response.CartItemResponse;
import com.example.trackingorders.entity.CartItems;
import com.example.trackingorders.entity.Inventory;
import com.example.trackingorders.entity.Products;
import com.example.trackingorders.exception.BusinessException;
import com.example.trackingorders.mapper.ProductsMapper;
import com.example.trackingorders.repository.CartItemsRepository;
import com.example.trackingorders.repository.InventoryRepository;
import com.example.trackingorders.service.CartsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CartsServiceImplement implements CartsService {

    private final CartItemsRepository cartItemsRepository ;
    private final ProductsMapper productsMapper ;
    private final InventoryRepository inventoryRepository ;

    @Override
    @Transactional(readOnly = true)
    public List<CartItemResponse> getMyCart() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName() ;

        List<CartItems> cartItems = cartItemsRepository.findCartItemsByUsername(username) ;
        List<Products> products = cartItems.stream().map(CartItems::getProducts).toList() ;
        List<Inventory> inventories = inventoryRepository.findInventoriesByProducts(products) ;

        Map<String, Inventory> inventoryMap = inventories.stream()
                .collect(Collectors.toMap(
                        inventory -> inventory.getProducts().getId(),
                        inventory -> inventory,
                        (existing, replacement) -> existing
                ));

        List<CartItemResponse> cartItemResponses = new ArrayList<>() ;

        for (int i = 0; i < cartItems.size(); i++) {
            CartItemResponse cartItemResponse = new CartItemResponse() ;
            CartItems cartItem = cartItems.get(i) ;
            Inventory inventory = inventoryMap.get(cartItem.getProducts().getId()) ;

            cartItemResponse.setId(cartItem.getId());
            cartItemResponse.setQuantity(cartItem.getQuantity());

            cartItemResponse.setProduct(productsMapper.toProductResponse(cartItem.getProducts(),inventory));

            if (cartItem.getQuantity() <= inventory.getQuantity() && inventory.getQuantity() > 10) {
                cartItemResponse.setStatus(StatusProductsEnum.IN_STOCK);
            }
            if (cartItem.getQuantity() <= inventory.getQuantity() && inventory.getQuantity() <=10 ) {
                cartItemResponse.setStatus(StatusProductsEnum.LIMITED_STOCK);
            }
            if(cartItem.getQuantity() > inventory.getQuantity()) {
                cartItemResponse.setStatus(StatusProductsEnum.OUT_OF_STOCK);
            }
            cartItemResponses.add(cartItemResponse) ;
        }

        return cartItemResponses;
    }

    @Override
    public CartItemResponse updateCartItem(String cartItemId,CartItemUpdateRequest request) {

        if (cartItemId == null) {
            throw new BusinessException("Field cartItem is null") ;
        }

        Optional<CartItems> cartItemsOptional = cartItemsRepository.findWithProductsById(cartItemId) ;
        if (cartItemsOptional.isEmpty()) {
            throw new BusinessException("Not found cartItem") ;
        }

        CartItems cartItem = cartItemsOptional.get() ;
        Integer quantityRequest = request.getQuantity() ;
        Inventory inventory = inventoryRepository.findInventoriesByProducts(cartItem.getProducts()) ;
        Integer stock = inventory.getQuantity() ;

        CartItemResponse cartItemResponse = new CartItemResponse() ;

        cartItemResponse.setId(cartItemId);
        cartItemResponse.setProduct(productsMapper.toProductResponse(cartItem.getProducts(),inventory));

        if (quantityRequest <= stock ) {
            cartItem.setQuantity(quantityRequest);
            if (stock <= 10) {
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

    @Override
    public void deleteCartItem(String id) {
        if (id == null) {
            throw new BusinessException("Field id is null");
        }

        Optional<CartItems> cartItemsOptional = cartItemsRepository.findById(id) ;
        if (cartItemsOptional.isEmpty()) {
            throw new BusinessException("Not found cartItem") ;
        }
        CartItems cartItem = cartItemsOptional.get();
        cartItem.setDeleted(true);
        cartItemsRepository.save(cartItem) ;
    }

}
