package com.backend.ms_books_catalogue.service;

import com.backend.ms_books_catalogue.controller.model.BooksQueryResponse;
import com.backend.ms_books_catalogue.dtos.CreateBookIndexRequest;
import com.backend.ms_books_catalogue.model.BookIndex;

public interface IBookOpenSearchService {
    BooksQueryResponse getBooks(String title, String author, String editorial, String genres, String rating, String price, Boolean aggregate);

    BookIndex getBook(String bookId);

    Boolean removeBook(String bookId);

    BookIndex createBook(CreateBookIndexRequest request);
}