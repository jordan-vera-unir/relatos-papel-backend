package com.actividad.booksPayments.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PurchaseDto {
	
	private String userId;
	private String libroId[];
	private double amount;
	private String paymentMethod;
    private boolean successfulPayment;

}
