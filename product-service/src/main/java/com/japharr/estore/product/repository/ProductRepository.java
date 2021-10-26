package com.japharr.estore.product.repository;

import com.japharr.estore.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {
    ProductEntity findByProductId(String productId);
    ProductEntity findByProductIdAndTitle(String productId, String title);
}
