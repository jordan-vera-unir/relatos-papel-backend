package com.backend.ms_books_catalogue.repository;

import com.backend.ms_books_catalogue.model.BookIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface IBookOpenSearchRepository extends ElasticsearchRepository<BookIndex, String> {

    List<BookIndex> findByTitle(String title);
}