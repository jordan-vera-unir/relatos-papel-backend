package com.actividad.booksPayments.service;

import com.actividad.booksPayments.Client.BookCatalogueClient;
import com.actividad.booksPayments.controller.model.CreatePurchaseRequest;
import com.actividad.booksPayments.data.PurchaseRepository;
import com.actividad.booksPayments.data.model.Purchase;
import com.actividad.booksPayments.data.model.PurchaseDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PurchaseServiceImpl implements IPurchaseService {

	@Autowired
	private PurchaseRepository repository;

	@Autowired
	private ObjectMapper objectMapper;

    @Autowired
    private BookCatalogueClient bookCatalogueClient;

    @Override
    public Purchase createPurchase(CreatePurchaseRequest request) {
        bookCatalogueClient.validateStock(request);
        bookCatalogueClient.updateBooks(request);
        return savePurchase(request);
    }

    private Purchase savePurchase(CreatePurchaseRequest request) {
        Purchase purchase = Purchase.builder()
                .userId(request.getUserId())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .successfulPayment(request.isSuccessfulPayment())
                .build();


        if (request.getPurchaseDetails() != null) {
            List<PurchaseDetails> details = request.getPurchaseDetails().stream().map(detailReq -> {
                return PurchaseDetails.builder()
                        .bookId(detailReq.getBookId())
                        .bookName(detailReq.getBookName())
                        .valuePaid(detailReq.getValuePaid())
                        .bookCount(detailReq.getBookCount())
                        .purchase(purchase)
                        .build();
            }).collect(Collectors.toList());


            purchase.setBooksDetails(details);
        }

        return repository.save(purchase);
    }

    @Override
    public Purchase getPurchase(String productId) {
        return repository.getIdTransaction(Long.valueOf(productId));
    }

    @Override
    public List<Purchase> getPurchasesUser(String userId) {
        return repository.getPaymentsUser(userId);
    }


}


