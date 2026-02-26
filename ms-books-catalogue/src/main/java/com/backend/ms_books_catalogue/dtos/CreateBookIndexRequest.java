package com.backend.ms_books_catalogue.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBookIndexRequest {
    private String title;
    private String author;
    private String editorial;
    private Integer pages;
    private String genres;
    private LocalDate publishedDate;
    private Short rating;
    private Double price;
    private String coverImage;
    private String dimensions;
    private Integer stock;
    private Boolean visible;
}
