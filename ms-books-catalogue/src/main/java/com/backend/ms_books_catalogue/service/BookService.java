package com.backend.ms_books_catalogue.service;

import com.backend.ms_books_catalogue.dtos.CreateBookRequest;
import com.backend.ms_books_catalogue.model.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService implements IBookService {
    private final BookRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    public List<Book> getBooks(String title, String author, String editorial, Boolean visible) {
        if (StringUtils.hasLength(title) || StringUtils.hasLength(author) || StringUtils.hasLength(editorial)
                || visible != null) {
            return repository.search(title, author, editorial, visible);
        }

        List<Book> books = repository.getBooks();
        return books.isEmpty() ? null : books;
    }

    @Override
    public Book getBook(String bookId) {
        return repository.getById(Long.valueOf(bookId));
    }

    @Override
    public Book createBook(CreateBookRequest request) {
        if (request != null && StringUtils.hasLength(request.getTitle().trim())
                && StringUtils.hasLength(request.getAuthor().trim())
                && StringUtils.hasLength(request.getEditorial().trim())
                && request.getPages() > 0
                && request.getGenres().size() > 0
                && request.getPublishedDate().isBefore(LocalDate.now())
                && request.getRating() >= 0 && request.getRating() < 6
                && request.getPrice() > 0
                && StringUtils.hasLength(request.getCoverImage().trim())
                && StringUtils.hasLength(request.getDimensions().trim())
                && request.getStock() >= 0
                && request.getVisible() != null) {

            Book book = Book.builder()
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

            return repository.save(book);
        } else {
            return null;
        }
    }

    @Override
    public Book updateBook(String bookId, CreateBookRequest request) {
        Book book = repository.getById(Long.valueOf(bookId));
        if (book == null) {
            return null;
        }
        book.update(request);
        repository.save(book);
        return book;
    }

    @Override
    public Book partialUpdateBook(String bookId, String partialBook) {
        Book book = repository.getById(Long.valueOf(bookId));
        if (book != null) {
            try {
                JsonMergePatch jsonMergePatch = JsonMergePatch.fromJson(objectMapper.readTree(partialBook));
                JsonNode target = jsonMergePatch.apply(objectMapper.readTree(objectMapper.writeValueAsString(book)));
                Book patched = objectMapper.treeToValue(target, Book.class);
                repository.save(patched);
                return patched;
            } catch (JsonProcessingException | JsonPatchException e) {
                log.error("Error updating book {}", bookId, e);
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public Boolean removeBook(String bookId) {
        Book book = repository.getById(Long.valueOf(bookId));

        if (book != null) {
            repository.delete(book);
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
