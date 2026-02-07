package com.actividad.booksPayments.data;


import com.actividad.booksPayments.data.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

interface IPurchaseJpaRepository extends JpaRepository<Purchase, Long>, JpaSpecificationExecutor<Purchase> {

    List<Purchase> findByUserId(String usuarioId);

}
