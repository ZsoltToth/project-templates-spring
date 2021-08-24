package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.AuthorDto;
import hu.uni.eku.tzs.controller.dto.AuthorMapper;
import hu.uni.eku.tzs.controller.dto.BookDto;
import hu.uni.eku.tzs.controller.dto.BookMapper;
import hu.uni.eku.tzs.model.Author;
import hu.uni.eku.tzs.service.BookManager;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/test")
    public AuthorDto hello() {
        BookMapper mapper = Mappers.getMapper(BookMapper.class);
        AuthorMapper authorMapper = Mappers.getMapper(AuthorMapper.class);
        return authorMapper.author2AuthorDto(new Author(0, "first", "last", "nat"));
    }

}
