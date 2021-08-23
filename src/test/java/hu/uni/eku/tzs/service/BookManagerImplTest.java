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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    void recordBookAlreadyExistsException() {
        // given
        Book hg2g = BookDataProvider.getHitchhikersGuide();
        BookEntity hg2gEntity = BookDataProvider.getHitchhikersGuideEntity();
        when(bookRepository.findById(BookDataProvider.HG2G_ISBN)).thenReturn(Optional.ofNullable(hg2gEntity));
        // when
        assertThatThrownBy(()->{
            service.record(hg2g);
        }).isInstanceOf(BookAlreadyExistsException.class);
    }

    private static class BookDataProvider{

        public static final String HG2G_ISBN = "1-85695-028-X";
        public static final String HG2G_TITLE = "The Hitchhiker's Guide to the Galaxy";

        public static Author getDouglasAdamsModel(){
            return new Author(1, "Douglas", "Adams", "English");
        }

        public static AuthorEntity getDouglasAdamsEntity(){
            return AuthorEntity.builder()
                .id(1)
                .firstName("Douglas")
                .lastName("Adams")
                .nationality("English")
                .build();
        }

       public static Book getHitchhikersGuide(){
           return new Book(HG2G_ISBN, getDouglasAdamsModel(), HG2G_TITLE, "English");
       }

       public static BookEntity getHitchhikersGuideEntity(){
           return BookEntity.builder()
               .isbn(HG2G_ISBN)
               .title(HG2G_TITLE)
               .author(getDouglasAdamsEntity())
               .language("English")
               .build();
       }
    }
}