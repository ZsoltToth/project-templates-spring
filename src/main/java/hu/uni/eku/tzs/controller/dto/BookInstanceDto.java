package hu.uni.eku.tzs.controller.dto;

import lombok.Data;

@Data
public class BookInstanceDto {

    private String inventoryNo;

    private BookDto book;

    private String state;
}
