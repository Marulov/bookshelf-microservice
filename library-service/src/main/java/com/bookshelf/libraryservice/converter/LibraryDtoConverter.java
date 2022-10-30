package com.bookshelf.libraryservice.converter;

import com.bookshelf.libraryservice.dto.LibraryDto;
import com.bookshelf.libraryservice.model.Library;
import org.springframework.stereotype.Component;

@Component
public class LibraryDtoConverter {

    public LibraryDto convert(Library library){
        return LibraryDto.builder()
                .id(library.getId())
                .build();
    }
}
