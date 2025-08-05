package com.multivendor.Product.Service.controller;

import com.multivendor.Product.Service.dto.BulkUploadResponse;
import com.multivendor.Product.Service.dto.ProductRequest;
import com.multivendor.Product.Service.dto.ProductResponse;
import com.multivendor.Product.Service.entity.Product;
import com.multivendor.Product.Service.service.IProductService;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @GetMapping("/hello")
    public String hello(){
        return "This is product service";
    }

    @PostMapping("/create")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest){
        ProductResponse productResponse = productService.createProduct(productRequest);

        return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
    }

    // Single SortBy
    @GetMapping("/getAllProducts")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy , @RequestParam(defaultValue = "asc") String sortDir){
        Page<ProductResponse> pageReturn =  productService.findAllProducts(page, size, sortBy, sortDir);

        return new ResponseEntity<>(pageReturn, HttpStatus.OK);
    }

    // Multiple SortBy
    @GetMapping("/getList")
    public ResponseEntity<Page<ProductResponse>> getAllProductsMultiple(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam List<String> sortBy , @RequestParam(defaultValue = "asc") String sortDir){
        Page<ProductResponse> pageReturn =  productService.findAllProductsMultiple(page, size, sortBy, sortDir);

        return new ResponseEntity<>(pageReturn, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){
        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
    }

    @GetMapping("/vendors/{vendorId}")
    public ResponseEntity<List<ProductResponse>> getProductByVendorId(@PathVariable Long vendorId){
        return new ResponseEntity<>(productService.getProductByVendorId(vendorId), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProduct(@RequestParam String query){
        return new ResponseEntity<>(productService.searchProduct(query), HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long productId, @RequestBody ProductRequest productRequest){
        return new ResponseEntity<>(productService.updateProduct(productId, productRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProductById(@PathVariable Long productId){
        productService.deleteProductById(productId);
        return new ResponseEntity<>("Product deleted", HttpStatus.OK);
    }

    @PostMapping("/bulk-upload")
    public ResponseEntity<BulkUploadResponse> bulkUploadOrUpdate(@Valid @RequestBody List<ProductRequest> productRequests){
        return new ResponseEntity<>(productService.bulkUploadOrUpdate(productRequests), HttpStatus.OK);
    }
}
