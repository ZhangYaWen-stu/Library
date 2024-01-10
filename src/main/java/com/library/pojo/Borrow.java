package com.library.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Borrow {
    Integer borrowId;
    Date borrowTime;
    Date dueTime;
    Date returnTime;
    Integer librarianJobNumber;
    Integer readerId;
    Integer bookId;
    String state;
}
