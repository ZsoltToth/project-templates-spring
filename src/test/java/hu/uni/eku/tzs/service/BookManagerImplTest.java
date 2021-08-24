package hu.uni.eku.tzs.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import hu.uni.eku.tzs.dao.AuthorRepository;
import hu.uni.eku.tzs.dao.BookRepository;
import hu.uni.eku.tzs.dao.entity.AuthorEntity;
import hu.uni.eku.tzs.dao.entity.BookEntity;
import hu.uni.eku.tzs.model.Author;
import hu.uni.eku.tzs.model.Book;
import hu.uni.eku.tzs.service.exceptions.BookAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.BookNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookManagerImplTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    AuthorRepository authorRepository;

    @InjectMocks
    BookManagerImpl service;

    @Test
    void recordBookHappyPath() throws BookAlreadyExistsException {
        // given
        Author douglasAdams = BookDataProvider.getDouglasAdamsModel();
        AuthorEntity douglasAddamsEntity = BookDataProvider.getDouglasAdamsEntity();
        Book hg2g = BookDataProvider.getHitchhikersGuide();
        BookEntity hg2gEntity = BookDataProvider.getHitchhikersGuideEntity();
        when(bookRepository.findById(any())).thenReturn(Optional.empty());
        when(authorRepository.findById(douglasAdams.getId())).thenReturn(Optional.ofNullable(douglasAddamsEntity));
        when(bookRepository.save(any())).thenReturn(hg2gEntity);
        // when
        Book actual = service.record(hg2g);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(hg2g);
//        assertThat(actual).isEqualToComparingFieldByFieldRecursively(hg2g);
    }

    @Test
    void recordBookUnknownAuthor() throws BookAlreadyExistsException {
        // given
        Author frankHerbert = BookDataProvider.getFrankHerbertModel();
        AuthorEntity frankHerbertEntity = BookDataProvider.getFrankHerbertEntity();
        Book dune = BookDataProvider.getDune();
        BookEntity duneEntity = BookDataProvider.getDuneEntity();
        when(bookRepository.findById(BookDataProvider.DUNE_ISBN)).thenReturn(Optional.empty());
        when(authorRepository.findById(frankHerbert.getId())).thenReturn(Optional.empty());
        when(authorRepository.save(frankHerbertEntity)).thenReturn(frankHerbertEntity);
        when(bookRepository.save(duneEntity)).thenReturn(duneEntity);
        // when
        Book actual = service.record(dune);
        // then
        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(dune);
    }

    @Test
    void recordBookAlreadyExistsException() {
        // given
        Book hg2g = BookDataProvider.getHitchhikersGuide();
        BookEntity hg2gEntity = BookDataProvider.getHitchhikersGuideEntity();
        when(bookRepository.findById(BookDataProvider.HG2G_ISBN)).thenReturn(Optional.ofNullable(hg2gEntity));
        // when
        assertThatThrownBy(() -> {
            service.record(hg2g);
        }).isInstanceOf(BookAlreadyExistsException.class);
    }

    @Test
    void readByIsbnHappyPath() throws BookNotFoundException {
        // given
        when(bookRepository.findById(BookDataProvider.HG2G_ISBN))
            .thenReturn(Optional.of(BookDataProvider.getHitchhikersGuideEntity()));
        Book expected = BookDataProvider.getHitchhikersGuide();
        // when
        Book actual = service.readByIsbn(BookDataProvider.HG2G_ISBN);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readByIsbnBookNotFoundException() {
        // given
        when(bookRepository.findById(BookDataProvider.UNKNOWN_ISBN)).thenReturn(Optional.empty());
        // when then
        assertThatThrownBy(() -> {
            service.readByIsbn(BookDataProvider.UNKNOWN_ISBN);
        }).isInstanceOf(BookNotFoundException.class)
            .hasMessageContaining(BookDataProvider.UNKNOWN_ISBN);
    }

    @Test
    void readAllHappyPath() {
        // given
        List<BookEntity> bookEntities = List.of(
            BookDataProvider.getDuneEntity(),
            BookDataProvider.getHitchhikersGuideEntity()
        );
        Collection<Book> expectedBooks = List.of(
            BookDataProvider.getDune(),
            BookDataProvider.getHitchhikersGuide()
        );
        when(bookRepository.findAll()).thenReturn(bookEntities);
        // when
        Collection<Book> actualBooks = service.readAll();
        // then
        assertThat(actualBooks)
            .usingRecursiveComparison()
            .isEqualTo(expectedBooks);
//            .containsExactlyInAnyOrderElementsOf(expectedBooks);
    }

    @Test
    void modifyBookHappyPath() {
        // given
        Book hg2g = BookDataProvider.getHitchhikersGuide();
        BookEntity hg2gEntity = BookDataProvider.getHitchhikersGuideEntity();
        when(bookRepository.save(hg2gEntity)).thenReturn(hg2gEntity);
        // when
        Book actual = service.modify(hg2g);
        // then
        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(hg2g);

    }

    private static class BookDataProvider {

        public static final String UNKNOWN_ISBN = "1-00000-000-X";

        public static final String HG2G_ISBN = "1-85695-028-X";

        public static final String HG2G_TITLE = "The Hitchhiker's Guide to the Galaxy";

        public static final String DUNE_ISBN = "978-0240807720";

        public static final String DUNE_TITLE = "Dune";


        public static Author getDouglasAdamsModel() {
            return new Author(1, "Douglas", "Adams", "English");
        }

        public static Author getFrankHerbertModel() {
            return new Author(2, "Frank", "Herbert", "American");
        }

        public static AuthorEntity getDouglasAdamsEntity() {
            return AuthorEntity.builder()
                .id(1)
                .firstName("Douglas")
                .lastName("Adams")
                .nationality("English")
                .build();
        }

        public static AuthorEntity getFrankHerbertEntity() {
            return AuthorEntity.builder()
                .id(2)
                .firstName("Frank")
                .lastName("Herbert")
                .nationality("American")
                .build();
        }

        public static Book getHitchhikersGuide() {
            return new Book(HG2G_ISBN, getDouglasAdamsModel(), HG2G_TITLE, "English");
        }

        public static Book getDune() {
            return new Book(DUNE_ISBN, getFrankHerbertModel(), DUNE_TITLE, "English");
        }

        public static BookEntity getHitchhikersGuideEntity() {
            return BookEntity.builder()
                .isbn(HG2G_ISBN)
                .title(HG2G_TITLE)
                .author(getDouglasAdamsEntity())
                .language("English")
                .build();
        }

        public static BookEntity getDuneEntity() {
            return BookEntity.builder()
                .isbn(DUNE_ISBN)
                .title(DUNE_TITLE)
                .author(getFrankHerbertEntity())
                .language("English")
                .build();
        }
    }
}