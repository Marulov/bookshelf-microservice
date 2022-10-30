package com.bookshelf.libraryservice.service;

import com.bookshelf.libraryservice.client.BookServiceClient;
import com.bookshelf.libraryservice.converter.LibraryDtoConverter;
import com.bookshelf.libraryservice.dto.AddBookRequest;
import com.bookshelf.libraryservice.dto.LibraryDto;
import com.bookshelf.libraryservice.exception.LibraryNotFoundException;
import com.bookshelf.libraryservice.model.Library;
import com.bookshelf.libraryservice.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final LibraryDtoConverter libraryDtoConverter;

    private final BookServiceClient bookServiceClient;

    public LibraryDto getAllBooksInLibraryById(Long id) {
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException("Library not found by id: " + id));

        return new LibraryDto(library.getId(),
                library.getUserBooks()
                        .stream()
                        .map(x -> bookServiceClient.getBookDetailsById(Long.valueOf(x)))
                        .map(ResponseEntity::getBody)
                        .collect(Collectors.toList()));
    }

    public LibraryDto createLibrary() {
        Library newLibrary = libraryRepository.save(new Library());
        return libraryDtoConverter.convert(newLibrary);
    }

    public void addBookToLibrary(AddBookRequest request) {
        Long bookId = bookServiceClient.getBookByIsbn(request.getIsbn()).getBody().getId();

        Library library = libraryRepository.findById(request.getId())
                .orElseThrow(() -> new LibraryNotFoundException("Library not found by id: " + request.getId()));

        library.getUserBooks().add(String.valueOf(bookId));
        libraryRepository.save(library);
    }
}