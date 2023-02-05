package com.bookshelf.bookservice.service;

import com.bookshelf.bookservice.converter.BookDtoConverter;
import com.bookshelf.bookservice.dto.BookDto;
import com.bookshelf.bookservice.dto.BookIdDto;
import com.bookshelf.bookservice.exception.BookNotFoundException;
import com.bookshelf.bookservice.model.Book;
import com.bookshelf.bookservice.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookService.class);
    private final BookRepository bookRepository;
    private final BookDtoConverter bookDtoConverter;

    public List<BookDto> getAllBooks() {
        return bookDtoConverter.convert(bookRepository.findAll());
    }

    public BookIdDto getBookByIsbn(String isbn) {

        LOGGER.info("Book request by isbn: {}", isbn);
        Book book = bookRepository.findBookByIsbn(isbn).orElseThrow(
                () -> new BookNotFoundException("Book not found by isbn: " + isbn));
        return new BookIdDto(book.getId(), book.getIsbn());
    }

    public BookDto getBookDetailsById(Long id) {
        return bookDtoConverter.convert(bookRepository.findById(id).orElseThrow(
                () -> new BookNotFoundException("Book not found by id: " + id)));
    }
}