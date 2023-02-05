package com.bookshelf.libraryservice.service;

import com.bookshelf.libraryservice.client.BookServiceClient;
import com.bookshelf.libraryservice.converter.LibraryDtoConverter;
import com.bookshelf.libraryservice.dto.BookDto;
import com.bookshelf.libraryservice.dto.LibraryDto;
import com.bookshelf.libraryservice.exception.LibraryNotFoundException;
import com.bookshelf.libraryservice.model.Library;
import com.bookshelf.libraryservice.repository.LibraryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class LibraryServiceTest {

    private LibraryService libraryService;
    private LibraryRepository libraryRepository;
    private LibraryDtoConverter libraryDtoConverter;
    private BookServiceClient bookServiceClient;

    @BeforeEach
    void setUp() {
        libraryRepository = Mockito.mock(LibraryRepository.class);
        libraryDtoConverter = Mockito.mock(LibraryDtoConverter.class);
        bookServiceClient = Mockito.mock(BookServiceClient.class);

        libraryService = new LibraryService(libraryRepository, libraryDtoConverter, bookServiceClient);
    }


    @DisplayName("Should Return LibraryDto With Detailed BookList With BookDto When LibraryId Exist")
    @Test
    void shouldReturnDetailedBookListWithBookDto_WhenLibraryIdExist() {
        Long id = 1L;
        List<String> userBooks = Arrays.asList("1", "2", "3");
        Library library = new Library(id, userBooks);
        BookDto book1 = new BookDto(1L, "title1", 2023, "author1", "press1", "isbn1");
        BookDto book2 = new BookDto(2L, "title2", 2022, "author2", "press2", "isbn2");
        BookDto book3 = new BookDto(3L, "title3", 2021, "author3", "press3", "isbn3");
        List<BookDto> bookDtoList = Arrays.asList(book1, book2, book3);
        LibraryDto expectedResult = new LibraryDto(id, bookDtoList);

        Mockito.when(libraryRepository.findById(id)).thenReturn(Optional.of(library));
        Mockito.when(bookServiceClient.getBookDetailsById(1L)).thenReturn(ResponseEntity.ok(book1));
        Mockito.when(bookServiceClient.getBookDetailsById(2L)).thenReturn(ResponseEntity.ok(book2));
        Mockito.when(bookServiceClient.getBookDetailsById(3L)).thenReturn(ResponseEntity.ok(book3));

        LibraryDto actualResult = libraryService.getAllBooksInLibraryById(id);

        assertEquals(expectedResult, actualResult);

        Mockito.verify(libraryRepository).findById(id);
        Mockito.verify(bookServiceClient).getBookDetailsById(1L);
        Mockito.verify(bookServiceClient).getBookDetailsById(2L);
        Mockito.verify(bookServiceClient).getBookDetailsById(3L);
        Mockito.verify(bookServiceClient, new Times(3)).getBookDetailsById(Mockito.any(Long.class));
    }

    @DisplayName("Should Throw LibraryNotFoundException When LibraryId Does Not Exist")
    @Test
    void shouldThrowLibraryNotFoundException_WhenLibraryIdDoesNotExist() {
        Long id = 1L;

        Mockito.when(libraryRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> libraryService.getAllBooksInLibraryById(id))
                .isInstanceOf(LibraryNotFoundException.class)
                .hasMessage("Library not found by id: " + id);

        Mockito.verify(libraryRepository).findById(id);
        Mockito.verifyNoInteractions(bookServiceClient);
    }

}