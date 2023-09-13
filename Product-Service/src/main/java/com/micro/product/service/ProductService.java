package com.micro.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micro.product.dto.ProductRequest;
import com.micro.product.dto.ProductResponse;
import com.micro.product.model.Product;
import com.micro.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {
	private final ProductRepository productRepository;
	private final ModelMapper modelMapper;
	private static final Logger log = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	public ProductService(ProductRepository productRepository, ModelMapper modelMapper) {
		this.productRepository = productRepository;
		this.modelMapper = modelMapper;
	}

	public void createProduct(ProductRequest productRequest) {
		Product product = modelMapper.map(productRequest, Product.class);
		System.out.println(product.getName()+product.getDescription());
		productRepository.save(product);
		log.info("Product {} is saved", product.getId());
	}

	public List<Product> getAllProducts() {
		List<Product> products = productRepository.findAll();
//		return products.stream().map(product -> modelMapper.map(product, ProductResponse.class))
//				.collect(Collectors.toList());
		return products;
	}
}