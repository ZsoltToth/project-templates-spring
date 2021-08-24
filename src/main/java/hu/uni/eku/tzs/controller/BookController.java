package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.BookDto;
import hu.uni.eku.tzs.controller.dto.BookMapper;
import hu.uni.eku.tzs.model.Book;
import hu.uni.eku.tzs.service.BookManager;
import hu.uni.eku.tzs.service.exceptions.BookAlreadyExistsException;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/books")
@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookManager bookManager;

    private final BookMapper bookMapper;

    @GetMapping(value = {"/", ""})
    public Collection<BookDto> readAllBooks() {
        return bookManager.readAll()
            .stream()
            .map(bookMapper::book2bookDto)
            .collect(Collectors.toList());

    }

    @PostMapping(value = { "", "/" })
    public BookDto create(@RequestBody BookDto recordRequestDto) {
        Book book = bookMapper.bookDto2Book(recordRequestDto);
        try {
            Book recordedBook = bookManager.record(book);
            return bookMapper.book2bookDto(recordedBook);
        } catch (BookAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping(value = { "", "/" })
    public BookDto update(@RequestBody BookDto updateRequestDto) {
        Book book = bookMapper.bookDto2Book(updateRequestDto);
        Book updatedBook = bookManager.modify(book);
        return bookMapper.book2bookDto(updatedBook);
    }

}
