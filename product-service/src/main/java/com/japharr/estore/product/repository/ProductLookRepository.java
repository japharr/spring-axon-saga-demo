package com.japharr.estore.product.repository;

import com.japharr.estore.product.entity.ProductLookupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLookRepository extends JpaRepository<ProductLookupEntity, String> {
    ProductLookupEntity findByProductIdOrTitle(String productId, String title);
}
