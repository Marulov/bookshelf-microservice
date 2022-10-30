package com.bookshelf.bookservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookIdDto {
    private Long id;
    private String isbn;
}