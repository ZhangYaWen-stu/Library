package com.library.mapper;

import com.library.pojo.Reservation;
import com.library.pojo.ReservationList;
import org.apache.ibatis.annotations.*;

import javax.swing.plaf.LabelUI;
import java.util.List;

@Mapper
public interface ReservationMapper {

    List<Reservation> getReservationList(Reservation reservation);

    List<ReservationList> getReservationListAll(Reservation reservation);

    @Select("select * from reservation where reservationId = #{id}")
    @ResultMap("reservationMap")
    Reservation getReservationById(Integer id);

    Reservation getReservation(Reservation reservation);

    @Select("select * from reservation where NOW() > reservation_deadline")
    @ResultMap("reservationMap")
    List<Reservation> getReservationOverdue();

    @Update("update reservation set state = 'overdue' where state = 'reserving' and NOW() > reservation_deadline")
    void setReservationsOverdue();

    @Update("update reservation set state = 'overdue' where reservationId = #{reservationId} and state = 'reserving'")
    void setReservationOverdue(Integer reservationId);

    @Update("update reservation set state = 'reserved' where reservationId = #{id} and state = 'reserving'")
    void setReservationSuccess(Integer id);

    @Insert("insert into reservation(readerId, isbn, reservation_time, reservation_deadline, librarian_job_number) " +
            "values(#{readerId}, #{isbn}, #{reservationTime}, #{reservationDeadline}, #{librarianJobNumber})")
    void addReservation(Reservation reservation);
    @Delete("delete from reservation where reservationId = #{reservationId}")
    void deleteReservationById(Integer id);

    void updateReservation(Reservation reservation);

    void deleteReservationList(Reservation reservation);
}
