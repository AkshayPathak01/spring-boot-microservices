package com.micro.product.repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.micro.product.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
}