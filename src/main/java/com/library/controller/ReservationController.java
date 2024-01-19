package com.library.controller;

import com.github.pagehelper.PageHelper;
import com.library.mapper.ReaderMapper;
import com.library.mapper.ReservationMapper;
import com.library.pojo.*;
import com.library.service.BookInfoService;
import com.library.service.BorrowService;
import com.library.service.ReservationService;
import com.library.util.LocalThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/Reservation")
public class ReservationController {

    @Autowired
    ReservationService reservationService;
    @Autowired
    BookInfoService bookInfoService;
    @Autowired
    BorrowService borrowService;

    @GetMapping("/deleteReservationReader")
    public Result deleteReservationReader(Integer reservationId) throws Exception {
        Map<String, Object> claim = LocalThread.get();
        Integer id = Integer.parseInt(claim.get("id").toString());
        Reservation reservation = new Reservation();
        reservation.setReaderId(id);
        reservation.setReservationId(reservationId);
        Reservation reservation_ = reservationService.getReservation(reservation);
        if(reservation_ == null){
            return Result.fail("不存在该条预约");
        }
        if(reservation_.getState().equals("reserved")){
            BookInfo bookInfo = new BookInfo();
            bookInfo.setIsbn(reservation_.getIsbn());
            bookInfo.setState("reserved");
            BookInfo bookInfo_ = bookInfoService.getBookInfo(bookInfo).get(0);
            bookInfo_.setState("canBorrow");
            bookInfoService.updateBookInfo(bookInfo_);
            borrowService.reservationRemind(bookInfo_.getIsbn());
        }
        reservationService.deleteReservation(reservationId);
        return Result.success();
    }

    @PostMapping("/addReservationReader")
    public Result addReservationReader(@RequestBody Reservation reservation){
        Map<String, Object> claim = LocalThread.get();
        Integer id = Integer.parseInt(claim.get("id").toString());
        Reservation reservation_ = new Reservation();
        reservation_.setState("reserving");
        reservation_.setReaderId(id);
        reservation_.setIsbn(reservation.getIsbn());
        if(!reservationService.queryReservation(reservation_).isEmpty()){
            return Result.fail("存在该条预约");
        }
        BookInfo bookInfo = new BookInfo();
        bookInfo.setIsbn(reservation.getIsbn());
        bookInfo.setState("canBorrow");
        List<BookInfo> bookInfos = bookInfoService.getBookInfo(bookInfo);
        if(!bookInfos.isEmpty()){
            return Result.fail("存在相应书目剩余");
        }
        reservation.setReaderId(id);
        reservation.setReservationTime(new Date());
        reservationService.addReservation(reservation);
        return Result.success();
    }


    @PostMapping("getReservationReader")
    public Result getReservationReader(Integer page, Integer pageSize, @RequestBody Reservation reservation){
        Map<String, Object> claim = LocalThread.get();
        PageHelper.startPage((page - 1) * pageSize + 1, pageSize);
        Integer id = Integer.parseInt(claim.get("id").toString());
        reservation.setReaderId(id);
        return Result.success(reservationService.queryReservationList(reservation));
    }

    @GetMapping("/admin/deleteReservation")
    public Result deleteReservation(Integer reservationId) throws Exception {
        Reservation reservation = new Reservation();
        reservation.setReservationId(reservationId);
        Reservation reservation_ = reservationService.getReservation(reservation);
        if(reservation_ == null){
            return Result.fail("不存在此种预约");
        }
        if(reservation_.getState().equals("reserved")){
            BookInfo bookInfo = new BookInfo();
            bookInfo.setIsbn(reservation_.getIsbn());
            bookInfo.setState("reserved");
            BookInfo bookInfo_ = bookInfoService.getBookInfo(bookInfo).get(0);
            bookInfo_.setState("canBorrow");
            bookInfoService.updateBookInfo(bookInfo_);
            borrowService.reservationRemind(bookInfo_.getIsbn());
        }
        reservationService.deleteReservationList(reservation);
        return Result.success();
    }
    @PostMapping("/admin/addReservation")
    public Result addReservation(@RequestBody Reservation reservation){
        Map<String, Object> claim = LocalThread.get();
        Integer id = Integer.parseInt(claim.get("id").toString());
        Reservation reservation_ = new Reservation();
        reservation_.setState("reserving");
        reservation_.setReaderId(reservation.getReaderId());
        reservation_.setIsbn(reservation.getIsbn());
        if(!reservationService.queryReservation(reservation_).isEmpty()){
            return Result.fail("存在该条预约");
        }
        BookInfo bookInfo = new BookInfo();
        bookInfo.setIsbn(reservation.getIsbn());
        bookInfo.setState("canBorrow");
        List<BookInfo> bookInfos = bookInfoService.getBookInfo(bookInfo);
        if(!bookInfos.isEmpty()){
            return Result.fail("存在相应书目剩余");
        }
        reservation.setReservationTime(new Date());
        reservation.setLibrarianJobNumber(id);
        reservationService.addReservation(reservation);
        return Result.success();
    }
    @PostMapping("/admin/queryReservation")
    public Result queryReservation(Integer page, Integer pageSize, @RequestBody Reservation reservation){
        PageHelper.startPage((page - 1) * pageSize + 1, pageSize);
        List<ReservationList> reservations = reservationService.queryReservationList(reservation);
        return Result.success(reservations);
    }
    @PostMapping("/admin/getReservationBook")
    public Result getReservationBook(@RequestBody Reimburse reimburse){
        Integer readerId = reimburse.getReaderId();
        String isbn = reimburse.getIsbn();
        Date dueTime = reimburse.getDueTime();
        Map<String, Object> claim = LocalThread.get();
        Integer id = Integer.parseInt(claim.get("id").toString());
        Reservation reservation = new Reservation();
        reservation.setReaderId(readerId);
        reservation.setIsbn(isbn);
        reservation.setState("reserved");
        List<Reservation> reservations = reservationService.queryReservation(reservation);
        if (reservations.isEmpty()){
            return Result.fail("不存在预约");
        }
        BookInfo bookInfo = new BookInfo();
        bookInfo.setIsbn(isbn);
        bookInfo.setState("reserved");
        List<BookInfo> bookInfos = bookInfoService.getBookInfo(bookInfo);
        if (bookInfos.isEmpty()){
            return Result.fail("不存在相应书目剩余");
        }
        reservationService.deleteReservation(reservations.get(0).getReservationId());
        Borrow borrow = new Borrow();
        borrow.setReaderId(readerId);
        borrow.setBookId(bookInfos.get(0).getId());
        borrow.setState("normal");
        borrow.setBorrowTime(new Date());
        borrow.setDueTime(dueTime);
        borrow.setLibrarianJobNumber(id);
        borrowService.addBorrow(borrow);
        return Result.success();
    }
}
