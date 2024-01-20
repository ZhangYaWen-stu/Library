package com.library.service.Implement;

import com.library.mapper.BookCatalogMapper;
import com.library.mapper.BookInfoMapper;
import com.library.mapper.ReaderMapper;
import com.library.mapper.ReservationMapper;
import com.library.pojo.*;
import com.library.service.BookInfoService;
import com.library.service.BorrowService;
import com.library.service.ReservationService;
import com.library.util.MailUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.System.out;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    ReservationMapper reservationMapper;
    @Autowired
    ReaderMapper readerMapper;
    @Autowired
    BookCatalogMapper bookCatalogMapper;
    @Autowired
    BorrowService borrowService;
    @Autowired
    BookInfoMapper bookInfoMapper;
    @Autowired
    MailUtil mailUtil;
    @Override
    public List<Reservation> reservationOverdue() {
        return reservationMapper.getReservationOverdue();
    }

    @Override
    public void deleteReservationList(Reservation reservation) {
        reservationMapper.deleteReservationList(reservation);
    }

    @Override
    public Reservation getReservation(Reservation reservation) {
        return reservationMapper.getReservation(reservation);
    }

    @Override
    public List<Reservation> queryReservation(Reservation reservation) {
        return reservationMapper.getReservationList(reservation);
    }

    @Override
    public void addReservation(Reservation reservation) {
        reservationMapper.addReservation(reservation);
    }

    @Override
    public void deleteReservation(Integer id) {
        reservationMapper.deleteReservationById(id);
    }

    @Override
    @Scheduled(fixedDelay = 1000 * 60 * 10)
    public void checkReservationOverdueInquiry() throws Exception{
        List<Reservation> reservations = reservationOverdue();
        for(Reservation reservation: reservations){
            String isbn = reservation.getIsbn();
            Integer reservationId = reservation.getReservationId();
            Integer readerId = reservation.getReaderId();
            reservationMapper.deleteReservationById(reservationId);
            BookCatalog bookCatalog = bookCatalogMapper.getBookCatalogByIsbn(isbn);
            Reader reader = readerMapper.getReaderById(readerId);
            mailUtil.sendReservationOverdue(reader, bookCatalog, reservation);
            if(reservation.getState().equals("reserved")){
                BookInfo bookInfo = new BookInfo();
                bookInfo.setIsbn(reservation.getIsbn());
                bookInfo.setState("reserved");
                BookInfo bookInfo_ = bookInfoMapper.getBookInfos(bookInfo).get(0);
                bookInfo_.setState("canBorrow");
                bookInfoMapper.updateBookInfo(bookInfo_);
                borrowService.reservationRemind(isbn);
            }
        }
    }
    @Override
    public void updateReservation(Reservation reservation) {
        reservationMapper.updateReservation(reservation);
    }

    @Override
    public List<ReservationList> queryReservationList(Reservation reservation) {
        return reservationMapper.getReservationListAll(reservation);
    }
}
