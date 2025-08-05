package com.multivendor.Product.Service.repository;


import com.multivendor.Product.Service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("Select p from Product p where LOWER(p.name) = LOWER(:name) and p.vendorId = :vendorId")
    Optional<Product> findByNameAndVendorIdIgnoreCase(@Param("name") String name, @Param("vendorId") Long vendorId);

    @Query("Select p from Product p where p.vendorId = :vendorId")
    List<Product> getProductByVendorId(@Param("vendorId") Long vendorId);

    @Query("SELECT p FROM Product p WHERE p.name  LIKE %:query% OR p.description  LIKE %:query% OR p.category  LIKE %:query%")
    List<Product> searchProductBy(@Param("query") String query);

    @Query("Select p FROM Product p WHERE p.vendorId = :vendorId AND LOWER(p.name) = LOWER(:name) ")
    Optional<Product> findProductByVendorIdAndName(@Param("vendorId") Long vendorId, @Param("name") String name);

    Page<Product> findAll(Pageable pageable);
}
