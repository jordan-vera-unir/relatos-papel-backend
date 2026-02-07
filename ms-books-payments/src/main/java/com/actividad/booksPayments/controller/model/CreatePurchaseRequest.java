package com.actividad.booksPayments.controller.model;

import com.actividad.booksPayments.data.model.PurchaseDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePurchaseRequest {

    private String userId;
    private List<PurchaseDetails> purchaseDetails;
    private double amount;
    private String paymentMethod;
    boolean successfulPayment;

}
