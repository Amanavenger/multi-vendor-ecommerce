package com.multivendor.Product.Service.service;


import com.multivendor.Product.Service.dto.BulkUploadResponse;
import com.multivendor.Product.Service.dto.ProductRequest;
import com.multivendor.Product.Service.dto.ProductResponse;
import com.multivendor.Product.Service.entity.Product;
import com.multivendor.Product.Service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImplProductService implements IProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Optional<Product> productExist = productRepository.findByNameAndVendorIdIgnoreCase(productRequest.getName(), productRequest.getVendorId());

        if (productExist.isPresent()) {
            throw new RuntimeException("Product already exists with ProductId: " +  productExist.get().getId());
        }
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setCategory(productRequest.getCategory());
        product.setVendorId(productRequest.getVendorId());
        product.setCreateTime(LocalDateTime.now());

        Product prod = productRepository.save(product);

        return productToResponse(prod);

    }
    //! Single SortBy
    @Override
    public Page<ProductResponse> findAllProducts(int page, int size, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> getPage =  productRepository.findAll(pageable);

        return getPage.map(product -> productToResponse(product));
    }

    //! SortBy Multiple column
    @Override
    public Page<ProductResponse> findAllProductsMultiple(int page, int size, List<String> sortBy, String sortDir) {

        List<Sort.Order> orders = new ArrayList<>();

        for(String sort:sortBy){
            orders.add(new Sort.Order(
                    sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sort));
        }

        Sort sort = Sort.by(orders);

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> getPage =  productRepository.findAll(pageable);

        return getPage.map(product -> productToResponse(product));
    }


    @Override
    public ProductResponse getProductById(Long id) {
        Optional<Product> productExist = productRepository.findById(id);

        if (productExist.isEmpty()) {
            throw new RuntimeException("Product does not exist with productId: " +  id);
        }

        return productToResponse(productExist.get());
    }

    @Override
    public List<ProductResponse> getProductByVendorId(Long vendorId) {
        List<Product> product = productRepository.getProductByVendorId(vendorId);

        if(product.isEmpty()){
            throw new RuntimeException("Vendor does not exist: " +  vendorId);
        }

        List<ProductResponse> productResponses = product.stream()
                .map(product1 -> productToResponse(product1)).collect(Collectors.toList());

        return productResponses;
    }

    @Override
    public List<ProductResponse> searchProduct(String query) {
        List<Product> products = productRepository.searchProductBy(query);

        if (products.isEmpty()) {
            throw new RuntimeException("No product exist: " + query);
        }

        List<ProductResponse> productResponses = products.stream()
                .map(product1 -> productToResponse(product1)).collect(Collectors.toList());

        return productResponses;
    }

    @Override
    public ProductResponse updateProduct(Long productId, ProductRequest productRequest) {
        Optional<Product> productExist = productRepository.findById(productId);

        if (productExist.isEmpty()) {
            throw new RuntimeException("Product does not exist with productId: " + productId);
        }

        productExist.get().setName(productRequest.getName());
        productExist.get().setDescription(productRequest.getDescription());
        productExist.get().setPrice(productRequest.getPrice());
        productExist.get().setStockQuantity(productRequest.getStockQuantity());
        productExist.get().setCategory(productRequest.getCategory());
        productExist.get().setVendorId(productRequest.getVendorId());
        productExist.get().setUpdateTime(LocalDateTime.now());
        Product prod = productRepository.save(productExist.get());
        return productToResponse(prod);
    }

    @Override
    public void deleteProductById(Long productId) {
        Optional<Product> productExist = productRepository.findById(productId);

        if (productExist.isEmpty()) {
            throw new RuntimeException("Product does not exist with productId: " + productId);
        }

        productRepository.delete(productExist.get());
    }

    @Override
    public BulkUploadResponse bulkUploadOrUpdate(List<ProductRequest> productRequests) {
        int updated = 0; int created = 0; int error = 0;

        for(ProductRequest productRequest : productRequests){
            Optional<Product> product = productRepository.findProductByVendorIdAndName(productRequest.getVendorId(), productRequest.getName());

            if(product.isPresent()){
                if(!isSameProduct(product.get(), productRequest)) {
                    updateProduct(product.get().getId(), productRequest);
                    updated++;
                }
            }else if(product.isEmpty()){
                createProduct(productRequest);
                created++;
            }else{
                error++;
            }
        }

        BulkUploadResponse bulkUploadResponse = new BulkUploadResponse();
        bulkUploadResponse.setUpdated(updated);
        bulkUploadResponse.setCreated(created);
        bulkUploadResponse.setErrors(error);

        return bulkUploadResponse;
    }


    private ProductResponse productToResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();

        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setPrice(product.getPrice());
        productResponse.setStockQuantity(product.getStockQuantity());
        productResponse.setCategory(product.getCategory());
        productResponse.setVendorId(product.getVendorId());
        productResponse.setCreatedAt(LocalDateTime.now());

        return productResponse;
    }

    private boolean isSameProduct(Product product, ProductRequest productRequest) {
       return product.getDescription().equals(productRequest.getDescription()) && product.getPrice().equals(productRequest.getPrice()) && product.getStockQuantity().equals(productRequest.getStockQuantity())
       && product.getCategory().equals(productRequest.getCategory());
    }
}
