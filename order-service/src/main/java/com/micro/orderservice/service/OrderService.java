package com.micro.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.micro.orderservice.dto.InventoryResponse;
import com.micro.orderservice.dto.OrderLineItemsDto;
import com.micro.orderservice.dto.OrderRequest;
import com.micro.orderservice.event.OrderPlacedEvent;
import com.micro.orderservice.model.Order;
import com.micro.orderservice.model.OrderLineItems;
import com.micro.orderservice.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class OrderService {
	private static final Logger log = LoggerFactory.getLogger(OrderService.class);
	
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private  WebClient.Builder webClientBuilder;
	@Autowired
	private KafkaTemplate<String,OrderPlacedEvent> kafkaTemplate;
	public void placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		
		order.setOrderNumber(UUID.randomUUID().toString());

		List<OrderLineItemsDto> orderLineItemsDtoList = orderRequest.getOrderLineItemsDtoList();
		if (orderLineItemsDtoList != null) {
		    List<OrderLineItems> orderLineItems = orderLineItemsDtoList.stream()
		            .map(this::mapToDto)
		            .toList();

		    order.setOrderLineItemsList(orderLineItems);
		   
		    List<String> skuCodes = order.getOrderLineItemsList().stream()
	                .map(OrderLineItems::getSkuCode)
	                .toList();

	        // Call Inventory Service, and place order if product is in
	        // stock
	        InventoryResponse[] inventoryResponsArray =  webClientBuilder.build().get()
	                .uri("http://inventory-service/api/inventory",
	                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
	                .retrieve()
	                .bodyToMono(InventoryResponse[].class)
	                .block();
	        if (inventoryResponsArray != null && inventoryResponsArray.length > 0) {
	        	boolean allProductsInStock = Arrays.stream(inventoryResponsArray)
		                .allMatch(InventoryResponse::isInStock);
		       
		        if(allProductsInStock){
		        	System.out.println("inside");
		            orderRepository.save(order);
		            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
		        } else {
		            throw new IllegalArgumentException("Product is not in stock, please try again later");
		        }
	        	 
	        } else {
	        	throw new IllegalArgumentException("Product is not in stock, please try again later");
	        }
	        
		} 
		else {
			 
			throw new IllegalArgumentException("Product is not in stock, please try again later");
		}


	}

	private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineItems;
	}

}
