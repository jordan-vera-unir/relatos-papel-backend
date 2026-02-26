package com.backend.ms_books_catalogue.controller;

import java.util.Map;

import com.backend.ms_books_catalogue.controller.model.BooksQueryResponse;
import com.backend.ms_books_catalogue.dtos.CreateBookIndexRequest;
import com.backend.ms_books_catalogue.model.BookIndex;
import com.backend.ms_books_catalogue.service.IBookOpenSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/books")
public class BookOpenSearchController {
    private final IBookOpenSearchService service;

    @GetMapping
    @Operation(
            operationId = "Obtener libros con parámetros de consulta",
            description = "Operación de lectura",
            summary = "Se devuelve una lista de todos los libros almacenados en la base de datos y que cumplan con los parametros de consulta.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookIndex.class)))
    public ResponseEntity<BooksQueryResponse> getBooks(
            @RequestHeader Map<String, String> headers,
            @Parameter(name = "title", description = "Titulo del libro. No tiene por que ser exacta", example = "Clean Code", required = false)
            @RequestParam(required = false) String title,
            @Parameter(name = "author", description = "Autor del libro. No tiene por que ser exacta", example = "Robert Martin", required = false)
            @RequestParam(required = false) String author,
            @Parameter(name = "editorial", description = "Editorial del libro. No tiene por que ser exacta", example = "Estupendo", required = false)
            @RequestParam(required = false) String editorial,
            @Parameter(name = "genres", description = "Géneros del libro.", example = "Drama,Suspenso", required = false)
            @RequestParam(required = false) String genres,
            @Parameter(name = "rating", description = "Calificación del libro.", example = "4", required = false)
            @RequestParam(required = false) String rating,
            @Parameter(name = "price", description = "Editorial del libro.", example = "30.5", required = false)
            @RequestParam(required = false) String price,
            @RequestParam(required = false, defaultValue = "false") Boolean aggregate) {

        log.info("headers: {}", headers);
        BooksQueryResponse books = service.getBooks(title, author, editorial, genres, rating, price, aggregate);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{bookId}")
    @Operation(
            operationId = "Obtener un libro",
            description = "Operación de lectura",
            summary = "Se devuelve un libro a partir de su identificador.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookIndex.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado el libro con el identificador indicado.")
    public ResponseEntity<BookIndex> getBook(@PathVariable String bookId) {

        log.info("Request received for book {}", bookId);
        BookIndex book = service.getBook(bookId);

        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/{bookId}")
    @Operation(
            operationId = "Eliminar un libro",
            description = "Operación de escritura",
            summary = "Se elimina un libro a partir de su identificador.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado el libro con el identificador indicado.")
    public ResponseEntity<Void> deleteBook(@PathVariable String bookId) {

        Boolean removed = service.removeBook(bookId);

        if (Boolean.TRUE.equals(removed)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping
    @Operation(
            operationId = "Insertar un libro",
            description = "Operación de escritura",
            summary = "Se crea un libro a partir de sus datos.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del libro a crear.",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateBookIndexRequest.class))))
    @ApiResponse(
            responseCode = "201",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookIndex.class)))
    @ApiResponse(
            responseCode = "400",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Datos incorrectos introducidos.")
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado el libro con el identificador indicado.")
    public ResponseEntity<BookIndex> getBook(@RequestBody CreateBookIndexRequest request) {

        BookIndex createdBook = service.createBook(request);

        if (createdBook != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
        } else {
            return ResponseEntity.badRequest().build();
        }

    }

}