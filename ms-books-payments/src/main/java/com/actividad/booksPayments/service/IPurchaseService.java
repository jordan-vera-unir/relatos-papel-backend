package com.actividad.booksPayments.service;

import com.actividad.booksPayments.controller.model.CreatePurchaseRequest;
import com.actividad.booksPayments.data.model.Purchase;

import java.util.List;

public interface IPurchaseService {

	Purchase createPurchase(CreatePurchaseRequest request);

    Purchase getPurchase(String productId);

    List<Purchase> getPurchasesUser(String userId);

}
