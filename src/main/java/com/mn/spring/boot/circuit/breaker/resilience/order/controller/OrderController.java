package com.mn.spring.boot.circuit.breaker.resilience.order.controller;

import java.net.URI;

import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.mn.spring.boot.circuit.breaker.resilience.order.domain.Inventory;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/order")
public class OrderController {
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@PostMapping("/check")
	@CircuitBreaker(name = "inventoryBreak",fallbackMethod = "inventoryFallBack")
	@ResponseBody
	public String checkOrder(@RequestBody Inventory inventory) {
		System.out.println("Inside checkOrder, for product "+inventory.getProductName());
		ResponseEntity<Boolean> exchange = restTemplate.exchange(new RequestEntity<Inventory>(inventory, HttpMethod.POST, URI.create("http://localhost:9090/inventory/check")), Boolean.class);
		if(exchange.getBody())
			return "Order placed Successfully";
		return "Order cannot be placed";
	}
	
	public String inventoryFallBack(Inventory inventory,Throwable t) {
		System.out.println("Inside fallback, for product "+inventory.getProductName());
		return "Product out of Stock";
	}
}
