package com.bookshelf.bookservice.service;

import com.bookshelf.bookservice.converter.BookDtoConverter;
import com.bookshelf.bookservice.dto.BookDto;
import com.bookshelf.bookservice.dto.BookIdDto;
import com.bookshelf.bookservice.exception.BookNotFoundException;
import com.bookshelf.bookservice.model.Book;
import com.bookshelf.bookservice.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookDtoConverter bookDtoConverter;

    public List<BookDto> getAllBooks() {
        return bookDtoConverter.convert(bookRepository.findAll());
    }

    public BookIdDto getBookByIsbn(String isbn) {
        Book book = bookRepository.findBookByIsbn(isbn).orElseThrow(
                () -> new BookNotFoundException("Book not found by isbn: " + isbn));
        return BookIdDto.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .build();
    }

    public BookDto getBookDetailsById(Long id) {
        return bookDtoConverter.convert(bookRepository.findById(id).orElseThrow(
                () -> new BookNotFoundException("Book not found by id: " + id)));
    }
}