package com.library.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    Integer reservationId;
    Date reservationTime;
    Date reservationDeadline;
    Integer librarianJobNumber;
    Integer readerId;
    String isbn;
    String state;
}
