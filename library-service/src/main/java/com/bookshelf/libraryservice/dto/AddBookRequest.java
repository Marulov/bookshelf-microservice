package com.bookshelf.libraryservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddBookRequest {
    private Long id;
    private String isbn;
}
