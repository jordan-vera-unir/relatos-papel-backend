package com.actividad.booksPayments.data.model;

import com.actividad.booksPayments.data.utils.Consts;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "purchases_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = Consts.TRANSACTION_ID, nullable = false)
    private Purchase purchase;

    @Column(name = Consts.BOOK_ID)
    private String bookId;

    @Column(name = Consts.BOOK_NAME)
    private String bookName;

    @Column(name = Consts.BOOK_COUNT)
    private int bookCount;

	@Column(name = Consts.VALUE_PAID)
	private double valuePaid;

}
