package hu.uni.eku.tzs.dao.entity;

import hu.uni.eku.tzs.model.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="books")
public class BookEntity {

    @Id
    private String isbn;

    @ManyToOne
    @JoinColumn(name="author")
    private AuthorEntity author;

    @Column(name="title")
    private String title;

    @Column(name="language")
    private String language;
}
