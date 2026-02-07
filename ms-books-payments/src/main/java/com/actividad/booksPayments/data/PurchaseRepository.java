package com.actividad.booksPayments.data;


import com.actividad.booksPayments.data.model.Purchase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PurchaseRepository {

    private final IPurchaseJpaRepository repository;

    public Purchase save(Purchase purchase) {
        return repository.save(purchase);
    }

    public Purchase getIdTransaction(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Purchase> getPaymentsUser(String userId) {
        return repository.findByUserId(userId);
    }

}
