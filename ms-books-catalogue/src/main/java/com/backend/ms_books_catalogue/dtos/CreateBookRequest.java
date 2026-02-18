package com.backend.ms_books_catalogue.dtos;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class CreateBookRequest {
    private String title;
    private String author;
    private String editorial;
    private Integer pages;
    private List<String> genres;
    private LocalDate publishedDate;
    private Integer rating;
    private Double price;
    private String coverImage;
    private String dimensions;
    private Integer stock;
    private Boolean visible;
}
