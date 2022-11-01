package com.bookshelf.libraryservice.client;

import com.bookshelf.libraryservice.dto.BookDto;
import com.bookshelf.libraryservice.dto.BookIdDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "book-service", path = "/api/v1/books")
public interface BookServiceClient {

    @GetMapping("/isbn/{isbn}")
    ResponseEntity<BookIdDto> getBookByIsbn(@PathVariable("isbn") String isbn);

    @GetMapping("/{id}")
    ResponseEntity<BookDto> getBookDetailsById(@PathVariable("id") Long id);
}