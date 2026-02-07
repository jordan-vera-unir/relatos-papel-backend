package com.actividad.booksPayments.data.model;

import com.actividad.booksPayments.data.utils.Consts;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Entity
@Table(name = "purchases")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Purchase {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transaction_id;
	
	@Column(name = Consts.USER_ID)
	private String userId;

    @JsonManagedReference
    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseDetails> booksDetails;

	@Column(name = Consts.AMOUNT)
	private double amount;

    @Column(name = Consts.PAYMENT_METHOD)
    private String paymentMethod;
	
	@Column(name = Consts.SUCCESSFUL_PAYMENT)
	private Boolean successfulPayment;

}

