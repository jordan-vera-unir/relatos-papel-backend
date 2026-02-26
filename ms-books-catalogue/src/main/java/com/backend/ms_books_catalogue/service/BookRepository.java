package com.backend.ms_books_catalogue.service;

import com.backend.ms_books_catalogue.model.Book;
import com.backend.ms_books_catalogue.utils.Consts;
import com.backend.ms_books_catalogue.utils.SearchCriteria;
import com.backend.ms_books_catalogue.utils.SearchOperation;
import com.backend.ms_books_catalogue.utils.SearchStatement;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookRepository {
    private final IBookRepository repository;

    public List<Book> getBooks() {
        return repository.findAll();
    }

    public Book getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Book save(Book Book) {
        return repository.save(Book);
    }

    public void delete(Book Book) {
        repository.delete(Book);
    }

    public List<Book> search(String name, String country, String description, Boolean visible) {
        SearchCriteria<Book> spec = new SearchCriteria<>();

        if (StringUtils.isNotBlank(name)) {
            spec.add(new SearchStatement(Consts.TITLE, name, SearchOperation.MATCH));
        }

        if (StringUtils.isNotBlank(country)) {
            spec.add(new SearchStatement(Consts.AUTHOR, country, SearchOperation.EQUAL));
        }

        if (StringUtils.isNotBlank(description)) {
            spec.add(new SearchStatement(Consts.EDITORIAL, description, SearchOperation.MATCH));
        }

        if (visible != null) {
            spec.add(new SearchStatement(Consts.VISIBLE, visible, SearchOperation.EQUAL));
        }

        return repository.findAll(spec);
    }
}
