package hu.uni.eku.tzs.service;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import hu.uni.eku.tzs.dao.BookInstanceRepository;
import hu.uni.eku.tzs.dao.BookRepository;
import hu.uni.eku.tzs.dao.entity.AuthorEntity;
import hu.uni.eku.tzs.dao.entity.BookEntity;
import hu.uni.eku.tzs.dao.entity.BookInstanceEntity;
import hu.uni.eku.tzs.model.Author;
import hu.uni.eku.tzs.model.Book;
import hu.uni.eku.tzs.model.BookInstance;
import hu.uni.eku.tzs.model.BookState;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookInstanceManagerImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookInstanceRepository bookInstanceRepository;

    @InjectMocks
    private BookInstanceManagerImpl service;

    @Test
    void readAllHappyPath() {
        // given
        when(bookInstanceRepository.findAll()).thenReturn(TestDataProvider.getDuneInstanceEntities());
        Collection<BookInstance> expected = TestDataProvider.getDuneInstances();
        // when
        Collection<BookInstance> actual = service.readAll();
        // then
        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(expected);
    }

    private static class TestDataProvider {

        public static final String DUNE_ISBN = "1-0000";

        public static Author getFrankHerber() {
            return new Author(0, "Frank", "Herber", "American");
        }

        public static AuthorEntity getFrankHerberEntity() {
            return new AuthorEntity(0, "Frank", "Herber", "American");
        }

        public static Book getDune() {
            return new Book(DUNE_ISBN, getFrankHerber(), "Dune", "English");
        }

        public static BookEntity getDuneEntity() {
            return new BookEntity(DUNE_ISBN, getFrankHerberEntity(), "Dune", "English");
        }

        public static List<BookInstance> getDuneInstances() {
            return List.of(
                new BookInstance("0", getDune(), BookState.BORROWABLE),
                new BookInstance("1", getDune(), BookState.BORROWABLE),
                new BookInstance("2", getDune(), BookState.BORROWABLE)
            );
        }

        public static List<BookInstanceEntity> getDuneInstanceEntities() {
            return List.of(
                new BookInstanceEntity("0", getDuneEntity(), BookState.BORROWABLE.toString()),
                new BookInstanceEntity("1", getDuneEntity(), BookState.BORROWABLE.toString()),
                new BookInstanceEntity("2", getDuneEntity(), BookState.BORROWABLE.toString())
            );
        }
    }
}