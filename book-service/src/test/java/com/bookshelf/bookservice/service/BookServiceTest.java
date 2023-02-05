package com.bookshelf.bookservice.service;

import com.bookshelf.bookservice.converter.BookDtoConverter;
import com.bookshelf.bookservice.dto.BookIdDto;
import com.bookshelf.bookservice.exception.BookNotFoundException;
import com.bookshelf.bookservice.model.Book;
import com.bookshelf.bookservice.repository.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {

    private BookService bookService;
    private BookRepository bookRepository;
    private BookDtoConverter bookDtoConverter;

    @BeforeEach
    void setUp() {
        bookRepository = Mockito.mock(BookRepository.class);
        bookDtoConverter = Mockito.mock(BookDtoConverter.class);

        bookService = new BookService(bookRepository, bookDtoConverter);
    }

    @DisplayName("Should return get book with BookDto if book found by isbn number")
    @Test
    void shouldReturnGetBookWithBookIdDto_WhenBookIsbnExist() {
        String isbn = "isbn";
        Long id = 1L;
        Book book = new Book(id, "title", 2023, "author", "press", isbn);
        BookIdDto expectedResult = new BookIdDto(id, isbn);

        Mockito.when(bookRepository.findBookByIsbn(isbn)).thenReturn(Optional.of(book));

        BookIdDto actualResult = bookService.getBookByIsbn(isbn);

        assertEquals(expectedResult, actualResult);

        Mockito.verify(bookRepository).findBookByIsbn(isbn);
        Mockito.verifyNoInteractions(bookDtoConverter);
    }

    @DisplayName("Should Throw BookNotFoundException When Book Does Not Exist By Isbn")
    @Test
    void shouldThrowBookNotFoundException_WhenBookIsbnDoesNotExist() {
        String isbn = "isbn";

        Mockito.when(bookRepository.findBookByIsbn(isbn)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> bookService.getBookByIsbn(isbn))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("Book not found by isbn: " + isbn);

        Mockito.verify(bookRepository).findBookByIsbn(isbn);
        Mockito.verifyNoInteractions(bookDtoConverter);
    }
}