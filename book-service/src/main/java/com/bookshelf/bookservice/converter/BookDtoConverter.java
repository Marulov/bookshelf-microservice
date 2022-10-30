package com.bookshelf.bookservice.converter;

import com.bookshelf.bookservice.dto.BookDto;
import com.bookshelf.bookservice.dto.BookIdDto;
import com.bookshelf.bookservice.model.Book;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookDtoConverter {

    public BookDto convert(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .bookYear(book.getBookYear())
                .author(book.getAuthor())
                .pressName(book.getPressName())
                .isbn(book.getIsbn())
                .build();
    }

    public List<BookDto> convert(List<Book> books) {
        return books.stream().map(this::convert).collect(Collectors.toList());
    }
}