package com.library.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationList {
    Integer reservationId;
    Date reservationTime;
    Date reservationDeadline;
    Integer librarianJobNumber;
    Integer readerId;
    String isbn;
    String state;
    String bookName;
    String userName;
}
