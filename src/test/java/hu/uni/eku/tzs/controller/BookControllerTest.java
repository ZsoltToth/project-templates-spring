package hu.uni.eku.tzs.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import hu.uni.eku.tzs.controller.dto.AuthorDto;
import hu.uni.eku.tzs.controller.dto.BookDto;
import hu.uni.eku.tzs.controller.dto.BookMapper;
import hu.uni.eku.tzs.model.Author;
import hu.uni.eku.tzs.model.Book;
import hu.uni.eku.tzs.service.BookManager;
import hu.uni.eku.tzs.service.exceptions.BookAlreadyExistsException;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookManager bookManager;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookController controller;

    @Test
    void readAllHappyPath() {
        // given
        when(bookManager.readAll()).thenReturn(List.of(TestDataProvider.getDune()));
        when(bookMapper.book2bookDto(any())).thenReturn(TestDataProvider.getDuneDto());
        Collection<BookDto> expected = List.of(TestDataProvider.getDuneDto());
        // when
        Collection<BookDto> actual = controller.readAllBooks();
        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);


    }

    @Test
    void createBookHappyPath() throws BookAlreadyExistsException {
        // given
        Book dune = TestDataProvider.getDune();
        BookDto duneDto = TestDataProvider.getDuneDto();
        when(bookMapper.bookDto2Book(duneDto)).thenReturn(dune);
        when(bookManager.record(dune)).thenReturn(dune);
        when(bookMapper.book2bookDto(dune)).thenReturn(duneDto);
        // when
        BookDto actual = controller.create(duneDto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(duneDto);
    }

    @Test
    void createBookThrowsBookAlreadyExistsException() throws BookAlreadyExistsException {
        // given
        Book dune = TestDataProvider.getDune();
        BookDto duneDto = TestDataProvider.getDuneDto();
        when(bookMapper.bookDto2Book(duneDto)).thenReturn(dune);
        when(bookManager.record(dune)).thenThrow(new BookAlreadyExistsException());
        // when then
        assertThatThrownBy(() -> {
            controller.create(duneDto);
        }).isInstanceOf(ResponseStatusException.class);
    }

    private static class TestDataProvider {

        public static final String DUNE_ISBN = "1-0000-000";

        public static Author getFrankHerbertModel() {
            return new Author(0, "Frank", "Herbert", "American");
        }

        public static AuthorDto getFrankHerbertDto() {
            return AuthorDto.builder()
                .id(0)
                .firstName("Frank")
                .lastName("Herbert")
                .nationality("American")
                .build();
        }

        public static Book getDune() {
            return new Book(DUNE_ISBN, getFrankHerbertModel(), "Dune", "English");
        }

        public static BookDto getDuneDto() {
            return BookDto.builder()
                .isbn(DUNE_ISBN)
                .author(getFrankHerbertDto())
                .title("Dune")
                .language("English")
                .build();
        }
    }


}