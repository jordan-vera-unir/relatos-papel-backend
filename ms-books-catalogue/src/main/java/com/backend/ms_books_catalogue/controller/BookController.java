package com.backend.ms_books_catalogue.controller;

import com.backend.ms_books_catalogue.dtos.CreateBookRequest;
import com.backend.ms_books_catalogue.model.Book;
import com.backend.ms_books_catalogue.service.BookService;
import com.backend.ms_books_catalogue.service.IBookOpenSearchService;
import com.backend.ms_books_catalogue.service.IBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {
    private final BookService service;

    @GetMapping
    @Operation(
            operationId = "Obtener libros con parametros de consulta",
            description = "Operacion de lectura",
            summary = "Se devuelve una lista de todos los libros almacenados en la base de datos y que cumplan con los parametros de consulta.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    public ResponseEntity<List<Book>> getBooks(
            @RequestHeader Map<String, String> headers,
            @Parameter(name = "title", description = "Titulo del libro. No tiene por que ser exacta", example = "Clean Code", required = false)
            @RequestParam(required = false) String title,
            @Parameter(name = "author", description = "Autor del libro. No tiene por que ser exacta", example = "Robert Martin", required = false)
            @RequestParam(required = false) String author,
            @Parameter(name = "editorial", description = "Editorial del libro. No tiene por que ser exacta", example = "Estupendo", required = false)
            @RequestParam(required = false) String editorial,
            @Parameter(name = "visible", description = "Estado del libro. true o false", example = "true", required = false)
            @RequestParam(required = false) Boolean visible) {

        log.info("headers: {}", headers);
        List<Book> books = service.getBooks(title, author, editorial, visible);

        if (books != null) {
            return ResponseEntity.ok(books);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/{bookId}")
    @Operation(
            operationId = "Obtener un libro",
            description = "Operacion de lectura",
            summary = "Se devuelve un libro a partir de su identificador.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado el libro con el identificador indicado.")
    public ResponseEntity<Book> getBook(@PathVariable String bookId) {
        log.info("Request received for book {}", bookId);
        Book book = service.getBook(bookId);

        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    @PostMapping
    @Operation(
            operationId = "Insertar un libro",
            description = "Operacion de escritura",
            summary = "Se crea un libro a partir de sus datos.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del libro a crear.",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateBookRequest.class))))
    @ApiResponse(
            responseCode = "201",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
            responseCode = "400",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Datos incorrectos introducidos.")
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado el libro con el identificador indicado.")
    public ResponseEntity<Book> addBook(@RequestBody CreateBookRequest request) {
        Book createdBook = service.createBook(request);

        if (createdBook == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @PatchMapping("/{bookId}")
    @Operation(
            operationId = "Modificar parcialmente un libro",
            description = "RFC 7386. Operacion de escritura",
            summary = "RFC 7386. Se modifica parcialmente un libro.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del libro a actualizar.",
                    required = true,
                    content = @Content(mediaType = "application/merge-patch+json", schema = @Schema(implementation = String.class))))
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
            responseCode = "400",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Libro inválido o datos incorrectos introducidos.")
    public ResponseEntity<Book> patchBook(@PathVariable String bookId, @RequestBody String partialBook) {
        Book patched = service.partialUpdateBook(bookId, partialBook);
        if (patched == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(patched);
    }

    @PutMapping("/{bookId}")
    @Operation(
            operationId = "Modificar totalmente un libro",
            description = "Operacion de escritura",
            summary = "Se modifica totalmente un libro.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del libro a actualizar.",
                    required = true,
                    content = @Content(mediaType = "application/merge-patch+json", schema = @Schema(implementation = CreateBookRequest.class))))
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Libro no encontrado.")
    public ResponseEntity<Book> updateBook(@PathVariable String bookId, @RequestBody CreateBookRequest body) {

        Book updated = service.updateBook(bookId, body);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{bookId}")
    @Operation(
            operationId = "Eliminar un libro",
            description = "Operacion de escritura",
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

        if (Boolean.FALSE.equals(removed)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
