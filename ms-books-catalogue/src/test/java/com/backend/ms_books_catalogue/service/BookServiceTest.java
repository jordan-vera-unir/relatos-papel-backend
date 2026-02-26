package com.backend.ms_books_catalogue.service;

import com.backend.ms_books_catalogue.dtos.CreateBookRequest;
import com.backend.ms_books_catalogue.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository repository;

    @Spy // Usamos Spy en ObjectMapper porque el servicio lo usa para lógica real de JSON
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private BookService bookService;

    // --- PRUEBAS DE CREACIÓN ---

    @Test
    @DisplayName("Debe crear un libro correctamente cuando el request es válido")
    void createBook_Success() {
        // Arrange
        CreateBookRequest request = createValidRequest();
        when(repository.save(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Book result = bookService.createBook(request);

        // Assert
        assertNotNull(result);
        assertEquals("Clean Code", result.getTitle());
        verify(repository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Debe retornar null si la fecha de publicación es futura")
    void createBook_FutureDate_ReturnsNull() {
        // Arrange
        CreateBookRequest request = createValidRequest();
        request.setPublishedDate(LocalDate.now().plusDays(1)); // Fecha futura

        // Act
        Book result = bookService.createBook(request);

        // Assert
        assertNull(result);
        verify(repository, never()).save(any());
    }

    // --- PRUEBAS DE BÚSQUEDA ---

    @Test
    @DisplayName("Debe retornar un libro por ID")
    void getBook_ExistingId_ReturnsBook() {
        // Arrange
        Book book = Book.builder().id(1L).title("Test").build();
        when(repository.getById(1L)).thenReturn(book);

        // Act
        Book result = bookService.getBook("1");

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    // --- PRUEBAS DE ELIMINACIÓN ---

    @Test
    @DisplayName("Debe retornar TRUE al eliminar un libro existente")
    void removeBook_Existing_ReturnsTrue() {
        // Arrange
        Book book = new Book();
        when(repository.getById(1L)).thenReturn(book);

        // Act
        Boolean result = bookService.removeBook("1");

        // Assert
        assertTrue(result);
        verify(repository).delete(book);
    }

    // Helper para crear requests válidos rápidamente
    private CreateBookRequest createValidRequest() {
        return CreateBookRequest.builder()
                .title("Clean Code")
                .author("Robert C. Martin")
                .editorial("Prentice Hall")
                .pages(464)
                .genres(List.of("Tech"))
                .publishedDate(LocalDate.now().minusYears(10))
                .rating(5)
                .price(40.0)
                .coverImage("url")
                .dimensions("20x15")
                .stock(10)
                .visible(true)
                .build();
    }
}