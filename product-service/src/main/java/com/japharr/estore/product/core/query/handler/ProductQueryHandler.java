package com.japharr.estore.productservice.core.query.handler;

import com.japharr.estore.productservice.core.query.FindProductsQuery;
import com.japharr.estore.productservice.entity.ProductEntity;
import com.japharr.estore.productservice.repository.ProductRepository;
import com.japharr.estore.productservice.model.ProductRestModel;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductQueryHandler {
    private final ProductRepository productRepository;

    public ProductQueryHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @QueryHandler
    public List<ProductRestModel> findProducts(FindProductsQuery query) {
        List<ProductRestModel> restModels = new ArrayList<>();

        List<ProductEntity> productEntities = productRepository.findAll();

        productEntities.forEach(r -> {
            ProductRestModel productRestModel = new ProductRestModel();
            BeanUtils.copyProperties(r, productRestModel);
            restModels.add(productRestModel);
        });

        return restModels;
    }
}
