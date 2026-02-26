package com.backend.ms_books_catalogue.controller.model;

import com.backend.ms_books_catalogue.model.BookIndex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BooksQueryResponse {

    private List<BookIndex> books;
    private List<AggregationDetails> aggs;

}