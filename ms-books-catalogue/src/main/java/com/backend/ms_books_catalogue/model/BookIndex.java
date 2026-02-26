package com.backend.ms_books_catalogue.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Document(indexName = "books", createIndex = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookIndex {

    @Id
    private String id;

    @Field(type = FieldType.Search_As_You_Type, name = "title")
    private String title;

    @Field(type = FieldType.Text, name = "author")
    private String author;

    @Field(type = FieldType.Keyword, name = "editorial")
    private String editorial;

    @Field(type = FieldType.Integer, name = "pages")
    private Integer pages;

    @Field(type = FieldType.Keyword, name = "genres")
    private String genres;

    @Field(type = FieldType.Date, format = DateFormat.date, name = "publishedDate")
    private LocalDate publishedDate;

    @Field(type = FieldType.Short, name = "rating")
    private Short rating;

    @Field(type = FieldType.Double, name = "price")
    private Double price;

    @Field(type = FieldType.Keyword, name = "coverImage", index = false)
    private String coverImage;

    @Field(type = FieldType.Text, name = "dimensions")
    private String dimensions;

    @Field(type = FieldType.Integer, name = "stock")
    private Integer stock;

    @Field(type = FieldType.Boolean, name = "visible")
    private Boolean visible;

}