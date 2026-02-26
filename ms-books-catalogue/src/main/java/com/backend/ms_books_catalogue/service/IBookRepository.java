package com.backend.ms_books_catalogue.service;

import com.backend.ms_books_catalogue.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    
}
