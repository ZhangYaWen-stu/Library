package com.library.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    String isbn;
    String location;
    String state;
    Integer librarianJobNumber;
    Integer id;
    String bookName;
    String author;
    String publisher;
    Date publishDate;
    String cover;
    Float unitPrice;
}
