package com.bookshelf.libraryservice.service;

import com.bookshelf.libraryservice.client.BookServiceClient;
import com.bookshelf.libraryservice.converter.LibraryDtoConverter;
import com.bookshelf.libraryservice.dto.AddBookRequest;
import com.bookshelf.libraryservice.dto.BookDto;
import com.bookshelf.libraryservice.dto.LibraryDto;
import com.bookshelf.libraryservice.exception.LibraryNotFoundException;
import com.bookshelf.libraryservice.model.Library;
import com.bookshelf.libraryservice.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryService.class);
    private final LibraryRepository libraryRepository;
    private final LibraryDtoConverter libraryDtoConverter;
    private final BookServiceClient bookServiceClient;

    public LibraryDto getAllBooksInLibraryById(Long id) {

        Library library = getLibraryById(id);
        LOGGER.info("[LibraryService] - Get oll books in library by id: {}", id);

        List<BookDto> bookList = library.getUserBooks()
                .stream()
                .map(bookId -> bookServiceClient.getBookDetailsById(Long.valueOf(bookId)))
                .map(ResponseEntity::getBody)
                .collect(Collectors.toList());

        return new LibraryDto(library.getId(), bookList);
    }

    public LibraryDto createLibrary() {
        Library newLibrary = libraryRepository.save(new Library());
        return libraryDtoConverter.convert(newLibrary);
    }

    public void addBookToLibrary(AddBookRequest request) {
        Long bookId = bookServiceClient.getBookByIsbn(request.getIsbn()).getBody().getId();

        Library library = getLibraryById(request.getId());
        LOGGER.info("[LibraryService] - Book add to library by isbn: {}", request.getIsbn());
        library.getUserBooks().add(String.valueOf(bookId));
        libraryRepository.save(library);
    }

    public Library getLibraryById(Long id) {
        return libraryRepository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException("Library not found by id: " + id));
    }
}