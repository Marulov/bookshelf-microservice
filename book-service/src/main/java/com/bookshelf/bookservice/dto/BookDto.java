package com.bookshelf.bookservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookDto {
    private Long id;
    private String title;
    private int bookYear;
    private String author;
    private String pressName;
    private String isbn;
}
