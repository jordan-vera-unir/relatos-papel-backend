package com.backend.ms_books_catalogue.model;

import com.backend.ms_books_catalogue.dtos.CreateBookRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "books")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "title is required")
    @Size(max = 60)
    private String title;

    @NotBlank(message = "author is required")
    private String author;

    private String editorial;

    @Min(value = 1, message = "Must have 1 page at least")
    private Integer pages;

    @NotEmpty(message = "Must have 1 genre at least")
    private List<String> genres;

    @NotNull
    @PastOrPresent(message = "Date must be previous today")
    private LocalDate publishedDate;

    @Min(1) @Max(5)
    private Integer rating;

    @DecimalMin(value = "0.0", message = "Price must be positive number")
    private Double price;

    @org.hibernate.validator.constraints.URL(message = "Image must be a valid url")
    private String coverImage;

    @NotNull
    private String dimensions;

    @Min(value = 0, message = "stock must be positive number")
    private Integer stock;

    @NotNull
    private Boolean visible = false;

    public void update(CreateBookRequest request) {
        this.title = request.getTitle();
        this.author = request.getAuthor();
        this.editorial = request.getEditorial();
        this.pages = request.getPages();
        this.genres = request.getGenres();
        this.publishedDate = request.getPublishedDate();
        this.rating = request.getRating();
        this.price = request.getPrice();
        this.coverImage = request.getCoverImage();
        this.dimensions = request.getDimensions();
        this.stock = request.getStock();
        this.visible = request.getVisible();
    }
}

