package com.bookshelf.libraryservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LibraryDto {
    private Long id;
    private List<BookDto> userBooks;
}