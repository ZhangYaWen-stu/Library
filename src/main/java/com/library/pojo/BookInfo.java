package com.library.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookInfo {
    String isbn;
    String location;
    String state;
    Integer librarianJobNumber;
    Integer id;
}
