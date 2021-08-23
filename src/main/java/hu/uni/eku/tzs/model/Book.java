package hu.uni.eku.tzs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Book {

    private String isbn;

    private Author author;

    private String title;

    private String language;
}
