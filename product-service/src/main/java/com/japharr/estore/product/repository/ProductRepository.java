package com.japharr.estore.productservice.repository;

import com.japharr.estore.productservice.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {
    ProductEntity findByProductId(String productId);
    ProductEntity findByProductIdAndTitle(String productId, String title);
}
