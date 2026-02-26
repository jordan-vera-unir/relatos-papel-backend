package com.backend.ms_books_catalogue.controller;

import java.util.Map;

import com.backend.ms_books_catalogue.controller.model.BooksQueryResponse;
import com.backend.ms_books_catalogue.dtos.CreateBookIndexRequest;
import com.backend.ms_books_catalogue.model.BookIndex;
import com.backend.ms_books_catalogue.service.IBookOpenSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/book")
public class BookOpenSearchController {
    @Autowired
    private final IBookOpenSearchService service;

    @GetMapping
    public ResponseEntity<BooksQueryResponse> getBooks(
            @RequestHeader Map<String, String> headers,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String editorial,
            @RequestParam(required = false) String genres,
            @RequestParam(required = false) String rating,
            @RequestParam(required = false) String price,
            @RequestParam(required = false, defaultValue = "false") Boolean aggregate) {

        log.info("headers: {}", headers);
        BooksQueryResponse books = service.getBooks(title, author, editorial, genres, rating, price, aggregate);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookIndex> getBook(@PathVariable String bookId) {

        log.info("Request received for product {}", bookId);
        BookIndex book = service.getBook(bookId);

        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String bookId) {

        Boolean removed = service.removeBook(bookId);

        if (Boolean.TRUE.equals(removed)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping
    public ResponseEntity<BookIndex> getProduct(@RequestBody CreateBookIndexRequest request) {

        BookIndex createdProduct = service.createBook(request);

        if (createdProduct != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } else {
            return ResponseEntity.badRequest().build();
        }

    }

}