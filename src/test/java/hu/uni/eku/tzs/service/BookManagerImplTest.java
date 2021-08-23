package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.AuthorRepository;
import hu.uni.eku.tzs.dao.BookRepository;
import hu.uni.eku.tzs.dao.entity.AuthorEntity;
import hu.uni.eku.tzs.dao.entity.BookEntity;
import hu.uni.eku.tzs.model.Author;
import hu.uni.eku.tzs.model.Book;
import hu.uni.eku.tzs.service.exceptions.BookAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BookManagerImplTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    AuthorRepository authorRepository;

    @InjectMocks
    BookManagerImpl service;

    @Test
    public void recordBookHappyPath() throws BookAlreadyExistsException {
        // given
        Author douglasAdams = new Author(1, "Douglas", "Adams", "English");
        AuthorEntity douglasAddamsEntity = AuthorEntity.builder()
                .id(1)
                .firstName("Douglas")
                .lastName("Adams")
                .nationality("English")
                .build();
        final String HG2G_ISBN = "1-85695-028-X";
        final String HG2G_TITLE = "The Hitchhiker's Guide to the Galaxy";
        Book hg2g = new Book(HG2G_ISBN, douglasAdams, HG2G_TITLE, "English");
        BookEntity hg2gEntity = BookEntity.builder()
                .isbn(HG2G_ISBN)
                .title(HG2G_TITLE)
                .author(douglasAddamsEntity)
                .language("English")
                .build();
        when(bookRepository.findById(any())).thenReturn(Optional.empty());
        when(authorRepository.findById(douglasAdams.getId())).thenReturn(Optional.ofNullable(douglasAddamsEntity));
        when(bookRepository.save(any())).thenReturn(hg2gEntity);
        // when
        Book actual = service.record(hg2g);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(hg2g);
//        assertThat(actual).isEqualToComparingFieldByFieldRecursively(hg2g);
    }
}