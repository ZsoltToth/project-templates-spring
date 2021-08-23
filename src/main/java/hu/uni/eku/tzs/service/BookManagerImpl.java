package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.AuthorRepository;
import hu.uni.eku.tzs.dao.BookRepository;
import hu.uni.eku.tzs.dao.entity.AuthorEntity;
import hu.uni.eku.tzs.dao.entity.BookEntity;
import hu.uni.eku.tzs.model.Author;
import hu.uni.eku.tzs.model.Book;
import hu.uni.eku.tzs.service.exceptions.BookAlreadyExistsException;
import java.util.Collection;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BookManagerImpl implements BookManager {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private static Book convertBookEntity2Model(BookEntity bookEntity) {
        return new Book(
            bookEntity.getIsbn(),
            new Author(
                bookEntity.getAuthor().getId(),
                bookEntity.getAuthor().getFirstName(),
                bookEntity.getAuthor().getLastName(),
                bookEntity.getAuthor().getNationality()),
            bookEntity.getTitle(),
            bookEntity.getLanguage()
        );
    }

    @Override
    public Book record(Book book) throws BookAlreadyExistsException {
        if (!bookRepository.findById(book.getIsbn()).isEmpty()) {
            throw new BookAlreadyExistsException();
        }
        AuthorEntity authorEntity = this.readOrRecordAuthor(book.getAuthor());
        BookEntity bookEntity = bookRepository.save(
            BookEntity.builder()
                .isbn(book.getIsbn())
                .author(authorEntity)
                .title(book.getTitle())
                .language(book.getLanguage())
                .build()
        );
        return BookManagerImpl.convertBookEntity2Model(bookEntity);
    }

    private AuthorEntity readOrRecordAuthor(Author author) {
        if (authorRepository.findById(author.getId()).isPresent()) {
            return authorRepository.findById(author.getId()).get();
        }
        return authorRepository.save(
            AuthorEntity.builder()
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .nationality(author.getNationality())
                .build()
        );
    }

    @Override
    public Book readByIsbn(String isbn) {
        return null;
    }

    @Override
    public Collection<Book> readAll() {
        return null;
    }

    @Override
    public Book modify(Book book) {
        return null;
    }

    @Override
    public void delete(Book book) {

    }
}
