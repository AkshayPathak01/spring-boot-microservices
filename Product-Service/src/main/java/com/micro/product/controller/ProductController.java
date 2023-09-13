package com.micro.product.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.micro.product.dto.ProductRequest;
import com.micro.product.dto.ProductResponse;
import com.micro.product.model.Product;
import com.micro.product.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/product")

public class ProductController {
	@Autowired
	private ProductService productService;

	@PostMapping
	public ResponseEntity<String> createProduct(@RequestBody ProductRequest productRequest) {
		try {
			productService.createProduct(productRequest);
			return ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating product");
		}
	}

	@GetMapping
	public ResponseEntity<List<Product>> getAllProducts() {
		try {
			List<Product> products = productService.getAllProducts();
			return ResponseEntity.status(HttpStatus.OK).body(products);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

}