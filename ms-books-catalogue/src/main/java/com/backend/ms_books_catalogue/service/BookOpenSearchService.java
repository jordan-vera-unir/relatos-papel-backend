package com.backend.ms_books_catalogue.service;

import com.backend.ms_books_catalogue.controller.model.BooksQueryResponse;
import com.backend.ms_books_catalogue.dtos.CreateBookIndexRequest;
import com.backend.ms_books_catalogue.model.BookIndex;
import com.backend.ms_books_catalogue.repository.BookOpenSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BookOpenSearchService implements IBookOpenSearchService {

    private final BookOpenSearchRepository repository;

    @Override
    public BooksQueryResponse getBooks(String title, String author, String editorial, String genres, String rating, String price, Boolean aggregate) {
        //Ahora por defecto solo devolvera productos visibles
        return repository.findBooks(title, author, editorial, genres, rating, price, aggregate);
    }

    @Override
    public BookIndex getBook(String productId) {
        return repository.findById(productId).orElse(null);
    }

    @Override
    public Boolean removeBook(String productId) {

        BookIndex product = repository.findById(productId).orElse(null);
        if (product != null) {
            repository.delete(product);
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public BookIndex createBook(CreateBookIndexRequest request) {

        if (request != null && StringUtils.hasLength(request.getTitle().trim())
                && StringUtils.hasLength(request.getAuthor().trim())
                && StringUtils.hasLength(request.getEditorial().trim())
                && request.getPages() > 0
                && !request.getGenres().isEmpty()
                && request.getPublishedDate().isBefore(LocalDate.now())
                && request.getRating() >= 0 && request.getRating() < 6
                && request.getPrice() > 0
                && StringUtils.hasLength(request.getCoverImage().trim())
                && StringUtils.hasLength(request.getDimensions().trim())
                && request.getStock() >= 0
                && request.getVisible() != null) {

            BookIndex product = BookIndex.builder()
                    .title(request.getTitle())
                    .author(request.getAuthor())
                    .editorial(request.getEditorial())
                    .pages(request.getPages())
                    .genres(request.getGenres())
                    .publishedDate(request.getPublishedDate())
                    .rating(request.getRating())
                    .price(request.getPrice())
                    .coverImage(request.getCoverImage())
                    .dimensions(request.getDimensions())
                    .stock(request.getStock())
                    .visible(request.getVisible()).build();

            return repository.save(product);
        } else {
            return null;
        }
    }

}