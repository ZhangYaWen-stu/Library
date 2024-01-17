package com.library.service;

import com.library.pojo.Reservation;
import com.library.pojo.ReservationList;

import java.util.List;

public interface ReservationService {

    public List<Reservation> reservationOverdue();
    public List<Reservation> queryReservation(Reservation reservation);
    public void addReservation(Reservation reservation);
    public void deleteReservation(Integer id);
    public Reservation getReservation(Reservation reservation);
    public void updateReservation(Reservation reservation);
    public void deleteReservationList(Reservation reservation);

    public void checkReservationOverdueInquiry() throws Exception;

    public List<ReservationList> queryReservationList(Reservation reservation);
}
