package com.bookshelf.libraryservice.service;

import com.bookshelf.libraryservice.converter.LibraryDtoConverter;
import com.bookshelf.libraryservice.dto.LibraryDto;
import com.bookshelf.libraryservice.exception.LibraryNotFoundException;
import com.bookshelf.libraryservice.model.Library;
import com.bookshelf.libraryservice.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final LibraryDtoConverter libraryDtoConverter;

    public LibraryDto getAllBooksInLibraryById(Long id) {
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException("Library not found by id: " + id));

        return libraryDtoConverter.convert(library);
    }

    public LibraryDto createLibrary() {
        Library newLibrary = libraryRepository.save(new Library());
        return libraryDtoConverter.convert(newLibrary);
    }
}
