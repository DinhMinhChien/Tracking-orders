package com.example.trackingorders.service.implement;

import com.example.trackingorders.entity.Inventory;
import com.example.trackingorders.entity.Products;
import com.example.trackingorders.exception.BusinessException;
import com.example.trackingorders.repository.InventoryRepository;
import com.example.trackingorders.service.InventoryService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class InventoryServiceImplement implements InventoryService {
    private final InventoryRepository inventoryRepository ;

    @Override
    public void decreaseStock(List<Products> products, Map<String, Integer> quantityByProductId) {
        List<Inventory> inventories = inventoryRepository.findInventoriesByProducts(products) ;

        for (Inventory inventory : inventories) {

            Products product = inventory.getProducts() ;
            Integer currentStock = inventory.getQuantity() ;
            Integer requestQuantity = quantityByProductId.get(product.getId()) ;

            if (currentStock < requestQuantity) {
                throw new BusinessException("Product " + product.getName() + " does not have enough stock") ;
            }
            inventory.setQuantity(currentStock - requestQuantity);
        }

        inventoryRepository.saveAll(inventories) ;
    }

    @Override
    @Transactional
    public void restoreQuantityProduct(List<Products> products, Map<String,Integer> quantityByProductId) {
        List<Inventory> inventories = inventoryRepository.findInventoriesByProducts(products) ;
        for (Inventory inventory : inventories) {
            inventory.setQuantity(inventory.getQuantity() + quantityByProductId.get(inventory.getProducts().getId()));
        }
        inventoryRepository.saveAll(inventories) ;
    }
}
