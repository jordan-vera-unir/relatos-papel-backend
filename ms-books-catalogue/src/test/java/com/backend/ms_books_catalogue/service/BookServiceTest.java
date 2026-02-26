package com.backend.ms_books_catalogue.service;

import com.backend.ms_books_catalogue.dtos.CreateBookIndexRequest;
import com.backend.ms_books_catalogue.model.BookIndex;
import com.backend.ms_books_catalogue.repository.BookOpenSearchRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookOpenSearchRepository repository;

    @Spy // Usamos Spy en ObjectMapper porque el servicio lo usa para lógica real de JSON
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private BookOpenSearchService bookService;

    String bookId = "syV6mJwBDlcDp9kMtUio";

    // --- PRUEBAS DE CREACIÓN ---

    @Test
    @DisplayName("Debe crear un libro correctamente cuando el request es válido")
    void createBook_Success() {
        // Arrange
        CreateBookIndexRequest request = createValidRequest();
        when(repository.save(any(BookIndex.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        BookIndex result = bookService.createBook(request);

        // Assert
        assertNotNull(result);
        assertEquals("Clean Code", result.getTitle());
        verify(repository, times(1)).save(any(BookIndex.class));
    }

    @Test
    @DisplayName("Debe retornar null si la fecha de publicación es futura")
    void createBook_FutureDate_ReturnsNull() {
        // Arrange
        CreateBookIndexRequest request = createValidRequest();
        request.setPublishedDate(LocalDate.now().plusDays(1)); // Fecha futura

        // Act
        BookIndex result = bookService.createBook(request);

        // Assert
        assertNull(result);
        verify(repository, never()).save(any());
    }

    // --- PRUEBAS DE BÚSQUEDA ---

    @Test
    @DisplayName("Debe retornar un libro por ID")
    void getBook_ExistingId_ReturnsBook() {
        // Arrange
        BookIndex book = BookIndex.builder().id(bookId).title("Test").build();
        when(repository.findById(bookId)).thenReturn(Optional.ofNullable(book));

        // Act
        BookIndex result = bookService.getBook("syV6mJwBDlcDp9kMtUio");

        // Assert
        assertNotNull(result);
        assertEquals(bookId, result.getId());
    }

    // --- PRUEBAS DE ELIMINACIÓN ---

    @Test
    @DisplayName("Debe retornar TRUE al eliminar un libro existente")
    void removeBook_Existing_ReturnsTrue() {
        // Arrange
        BookIndex book = new BookIndex();
        when(repository.findById(bookId)).thenReturn(Optional.of(book));

        // Act
        Boolean result = bookService.removeBook(bookId);

        // Assert
        assertTrue(result);
        verify(repository).delete(book);
    }

    // Helper para crear requests válidos rápidamente
    private CreateBookIndexRequest createValidRequest() {
        return CreateBookIndexRequest.builder()
                .title("Clean Code")
                .author("Robert C. Martin")
                .editorial("Prentice Hall")
                .pages(464)
                .genres("Tech")
                .publishedDate(LocalDate.now().minusYears(10))
                .rating((short) 5)
                .price(40.0)
                .coverImage("url")
                .dimensions("20x15")
                .stock(10)
                .visible(true)
                .build();
    }
}