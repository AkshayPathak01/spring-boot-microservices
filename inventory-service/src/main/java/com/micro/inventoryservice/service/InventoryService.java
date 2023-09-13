package com.micro.inventoryservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micro.inventoryservice.controller.InventoryController;
import com.micro.inventoryservice.dto.InventoryResponse;
import com.micro.inventoryservice.model.Inventory;
import com.micro.inventoryservice.repository.InventoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
@Service
@Slf4j
public class InventoryService {
	@Autowired
	private InventoryRepository inventoryRepository;
	private static final Logger log = LoggerFactory.getLogger(InventoryController.class);

	public List<InventoryResponse> isInStock(List<String> skuCode) {
		log.info("Received inventory check request for no : {}", skuCode);
		List<Inventory> list = inventoryRepository.findAll();
//		List<InventoryResponse> responseList = inventoryRepository.findBySkuCodeIn(skuCode).stream()
//				.map(inventory -> InventoryResponse.builder().skuCode(inventory.getSkuCode())
//						.isInStock(inventory.getQuantity() > 0).build())
//				.toList();

		List<InventoryResponse> responseList2 =list.stream().map(inventory -> InventoryResponse.builder().skuCode(inventory.getSkuCode())
				.isInStock(inventory.getQuantity() > 0).build()).toList();
		
		List<InventoryResponse> inStockItems = responseList2.stream()
			    .filter(InventoryResponse::isInStock)
			    .collect(Collectors.toList());
		List<InventoryResponse> matchedItems = inStockItems.stream()
			    .filter(item -> skuCode.contains(item.getSkuCode()))
			    .collect(Collectors.toList());
		System.out.println(matchedItems);
		return matchedItems;

	}

}
*/

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

   // @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {
         List<InventoryResponse> re=inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .skuCode(inventory.getSkuCode())
                                .isInStock(inventory.getQuantity() > 0)
                                .build()
                ).toList();
         return re;
    }
}

