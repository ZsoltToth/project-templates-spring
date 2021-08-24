package hu.uni.eku.tzs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Author {

    private int id;

    private String firstName;

    private String lastName;

    private String nationality;
}
