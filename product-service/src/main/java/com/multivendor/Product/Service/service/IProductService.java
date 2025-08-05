package com.multivendor.Product.Service.service;

import com.multivendor.Product.Service.dto.BulkUploadResponse;
import com.multivendor.Product.Service.dto.ProductRequest;
import com.multivendor.Product.Service.dto.ProductResponse;
import com.multivendor.Product.Service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductService {
    ProductResponse createProduct(ProductRequest productRequest);

    Page<ProductResponse> findAllProducts(int page, int size, String sortBy, String sortDir);

    Page<ProductResponse> findAllProductsMultiple(int page, int size, List<String> sortBy, String sortDir);

    ProductResponse getProductById(Long id);

    List<ProductResponse> getProductByVendorId(Long vendorId);

    List<ProductResponse> searchProduct(String query);

    ProductResponse updateProduct(Long productId, ProductRequest productRequest);

    void deleteProductById(Long productId);

    BulkUploadResponse bulkUploadOrUpdate(List<ProductRequest> productRequests);
}
