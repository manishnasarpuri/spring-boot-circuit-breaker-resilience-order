package com.mn.spring.boot.circuit.breaker.resilience.order.domain;

import lombok.Data;

@Data
public class Inventory {
	private String productName;
	private int unit;
}
