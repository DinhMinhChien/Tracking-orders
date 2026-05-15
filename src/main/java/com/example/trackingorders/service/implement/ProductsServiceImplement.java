package com.example.trackingorders.service.implement;

import com.example.trackingorders.common.StatusProductsEnum;
import com.example.trackingorders.dto.ProductsExportDTO;
import com.example.trackingorders.dto.request.ProductCreateRequest;
import com.example.trackingorders.dto.request.ProductsUpdateRequest;
import com.example.trackingorders.dto.response.ProductCreateResponse;
import com.example.trackingorders.dto.response.ProductDashboardStats;
import com.example.trackingorders.dto.response.ProductsResponse;
import com.example.trackingorders.entity.Inventory;
import com.example.trackingorders.entity.Products;
import com.example.trackingorders.exception.BusinessException;
import com.example.trackingorders.mapper.ProductsMapper;
import com.example.trackingorders.repository.InventoryRepository;
import com.example.trackingorders.repository.ProductsRepository;
import com.example.trackingorders.service.ProductsService;
import com.example.trackingorders.service.specfication.ProductsSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ProductsServiceImplement implements ProductsService {
    private final ProductsRepository productsRepository ;
    private final InventoryRepository inventoryRepository ;
    private final ProductsMapper productsMapper ;
    @Override
    public ProductCreateResponse create(ProductCreateRequest request) {

        Products products = new Products() ;

        products.setName(request.getName());
        products.setPrice(request.getPrice());
        products.setDescription(request.getDescription());
        products.setIsFeature(request.getIsFeature());

        Integer quantity = request.getQuantity() ;
        if ( quantity == 0) {
            products.setStatus(StatusProductsEnum.OUT_OF_STOCK);
        }
        if ( quantity <= 10) {
            products.setStatus(StatusProductsEnum.LOW_STOCK);
        }
        if ( quantity > 10 ) {
            products.setStatus(StatusProductsEnum.IN_STOCK);
        }

        products.setSku(request.getSku()) ;
        products.setImgUrl(request.getImgUrl());

        Products productsSave = productsRepository.save(products) ;

        Inventory inventory = new Inventory() ;
        inventory.setProducts(productsSave);
        inventory.setQuantity(quantity);
        inventoryRepository.save(inventory) ;

        ProductCreateResponse response = productsMapper.toResponse(productsSave) ;
        response.setQuantity(quantity);
        return response;

    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductsResponse> getAll(StatusProductsEnum status, String keyword,Boolean isFeature, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber-1,pageSize) ;
        Specification<Products> specification = Specification.where(null) ;
        if (status != null) {
            specification = specification.and(ProductsSpecification.likeStatus(status)) ;
        }
        if (keyword != null ) {
            specification = specification.and(ProductsSpecification.likeSku(keyword)) ;
        }
        if (isFeature != null) {
            specification = specification.and(ProductsSpecification.EqualIsFeature(isFeature));
        }
        Page<Products> products = productsRepository.findAll(specification,pageable) ;
        List<Inventory> inventories = inventoryRepository.findInventoriesByProducts(products.stream().toList()) ;

        Map<String, Inventory> inventoryMap = inventories.stream()
                .collect(Collectors.toMap(
                        inv -> inv.getProducts().getId(),
                        inv -> inv,
                        (existing, replacement) -> existing
                ));

        Page<ProductsResponse> responses = products.map(entity -> {
                Inventory inventory = inventoryMap.get(entity.getId()) ;
                return productsMapper.toProductResponse(entity,inventory);
            }
        ) ;
        return responses ;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDashboardStats getStats() {
        ProductDashboardStats response = productsRepository.getInventoryDashboardStats() ;
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductsResponse getDetail(String id) {
        if (id == null) {
            throw new BusinessException("Field id is null") ;
        }

        Optional<Products> productsOptional = productsRepository.findById(id) ;

        if (productsOptional.isEmpty()) {
            throw new BusinessException("Not exist product");
        }

        Products product = productsOptional.get() ;

        Inventory inventory = inventoryRepository.findInventoriesByProducts(product) ;
        ProductsResponse response = productsMapper.toProductResponse(productsOptional.get(),inventory) ;

        return response ;
    }

    @Override
    public ProductsResponse update(String id, ProductsUpdateRequest request) {
        if (id == null) {
            throw new BusinessException("Field id is null") ;
        }
        Optional<Products> productsOptional = productsRepository.findById(id) ;

        if (productsOptional.isEmpty()) {
            throw new BusinessException("Not found product") ;
        }

        Products product = new Products() ;

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setSku(request.getSku());
        Inventory inventory = inventoryRepository.findInventoriesByProducts(product) ;
        inventory.setQuantity(request.getQuantity());
        Products productSave = productsRepository.save(product) ;

        inventoryRepository.save(inventory) ;
        ProductsResponse response = productsMapper.toProductResponse(productSave,inventory) ;

        return response ;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductsExportDTO> getDataForExport(StatusProductsEnum status, String keyword) {
        Specification<Products> specification = Specification.where(null) ;
        if (status != null) {
            specification = specification.and(ProductsSpecification.likeStatus(status)) ;
        }
        if (keyword != null) {
            specification = specification.and(ProductsSpecification.likeSku(keyword)) ;
        }
        List<Products> products = productsRepository.findAll(specification) ;
        List<Inventory> inventories = inventoryRepository.findInventoriesByProducts(products) ;
        Map<String, Inventory> inventoryMap = inventories.stream()
                .collect(Collectors.toMap(
                        inv -> inv.getProducts().getId(),
                        inv -> inv,
                        (existing, replacement) -> existing
                ));
        List<ProductsExportDTO> productsExportDTOs = products.stream().map(product -> productsMapper.toExportDTO(product,inventoryMap.get(product.getId()))).toList();
        return productsExportDTOs ;
    }
}
