package hu.uni.eku.tzs.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookInstanceDto {

    private String inventoryNo;

    private BookDto book;

    private String state;
}
