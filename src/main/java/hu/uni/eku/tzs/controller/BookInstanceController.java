package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.BookDto;
import hu.uni.eku.tzs.controller.dto.BookInstanceDto;
import hu.uni.eku.tzs.controller.dto.BookInstanceMapper;
import hu.uni.eku.tzs.controller.dto.BookMapper;
import hu.uni.eku.tzs.model.Book;
import hu.uni.eku.tzs.model.BookInstance;
import hu.uni.eku.tzs.service.BookInstanceManager;
import hu.uni.eku.tzs.service.BookManager;
import hu.uni.eku.tzs.service.exceptions.BookNotFoundException;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/books/instances")
public class BookInstanceController {

    private final BookManager bookManager;

    private final BookInstanceManager bookInstanceManager;

    private final BookInstanceMapper bookInstanceMapper;

    private final BookMapper bookMapper;

    @GetMapping("/")
    public Collection<BookInstanceDto> readAllBookInstances() {
        return bookInstanceManager.readAll()
            .stream()
            .map(bookInstanceMapper::bookInstance2BookInstanceDto)
            .collect(Collectors.toList());
    }

    @PostMapping("/")
    public BookInstanceDto create(@RequestBody BookDto bookDto){
        Book book = bookMapper.bookDto2Book(bookDto);
        log.debug(book.toString());
        try {
            BookInstance recordedBookInstance = bookInstanceManager.record(book);
            return bookInstanceMapper.bookInstance2BookInstanceDto(recordedBookInstance);
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
