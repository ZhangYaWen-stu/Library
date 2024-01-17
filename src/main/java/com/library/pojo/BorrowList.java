package com.library.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowList {
    Integer borrowId;
    Date borrowTime;
    Date dueTime;
    Date returnTime;
    Integer librarianJobNumber;
    Integer readerId;
    Integer bookId;
    String state;
    String bookName;
    String userName;
}
