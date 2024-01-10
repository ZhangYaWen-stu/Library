package com.library.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookCatalog {
    String bookName;
    String author;
    String publisher;
    Date publishDate;
    Integer totalCopies;
    String cover;
    Integer librarianJobNumber;
    String isbn;
    Float unitPrice;
    Integer canBorrow;
}
