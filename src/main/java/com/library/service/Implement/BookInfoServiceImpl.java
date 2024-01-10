package com.library.service.Implement;

import com.library.mapper.BookInfoMapper;
import com.library.mapper.BorrowMapper;
import com.library.mapper.ReservationMapper;
import com.library.pojo.*;
import com.library.service.*;
import com.library.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookInfoServiceImpl implements BookInfoService {

    @Autowired
    BookInfoMapper bookInfoMapper;
    @Autowired
    BookCatalogService bookCatalogService;
    @Autowired
    BorrowMapper borrowMapper;
    @Autowired
    ReservationMapper reservationMapper;
    @Autowired
    ReservationService reservationService;
    @Autowired
    UserService userService;
    @Autowired
    MailUtil mailUtil;

    @Override
    public List<BookInfo> getBookInfo(BookInfo bookInfo) {
        return bookInfoMapper.getBookInfos(bookInfo);
    }

    @Override
    public void updateBookInfo(BookInfo bookInfo) throws Exception{
        BookInfo bookInfo_ = bookInfoMapper.getBookInfoById(bookInfo.getId());
        bookInfoMapper.updateBookInfo(bookInfo);
        if(bookInfo.getState() != null)
        {
            if(bookInfo_.getState().equals("cantBorrow") || bookInfo_.getState().equals("canBorrow")){
                if(bookInfo.getState().equals("canBorrow") && bookInfo_.getState().equals("cantBorrow")){
                    reservationReminder(bookInfo.getIsbn());
                }
                if(bookInfo.getState().equals("cantBorrow") && bookInfo_.getState().equals("canBorrow")){
                    bookCatalogService.setCanBorrow(-1, bookInfo_.getIsbn());
                }
            }
        }

    }

    @Override
    public void addBookInfo(BookInfo bookInfo) throws Exception {
        if(bookInfo.getLocation().equals("图书流通室")){
            bookInfo.setState("canBorrow");
            bookInfoMapper.addBookInfo(bookInfo);
            reservationReminder(bookInfo.getIsbn());
        }
        if(bookInfo.getLocation().equals("图书阅览室")){
            bookInfo.setState("cantBorrow");
            bookInfoMapper.addBookInfo(bookInfo);
        }
        bookCatalogService.setTotalBorrow(1, bookInfo.getIsbn());
    }

    @Override
    public void deleteBookInfo(BookInfo bookInfo) {
        List<BookInfo> bookInfos = bookInfoMapper.getBookInfos(bookInfo);
        for (BookInfo bookInfo_ : bookInfos)
        {
            if (bookInfo_.getState().equals("canBorrow") || bookInfo_.getState().equals("cantBorrow")) {
                if(bookInfo_.getState().equals("canBorrow"))
                {
                    bookCatalogService.setCanBorrow(-1, bookInfo_.getIsbn());
                }
                bookCatalogService.setTotalBorrow(-1, bookInfo_.getIsbn());
            }
        }
        bookInfoMapper.deleteBookInfo(bookInfo);
    }

    @Override
    public void reservationReminder(String isbn) throws Exception {
        Reservation reservation = new Reservation();
        reservation.setIsbn(isbn);
        reservation.setState("reserving");
        BookInfo bookInfo = new BookInfo();
        List<Reservation> reservations = reservationService.queryReservation(reservation);
        for(Reservation reservation_ : reservations){
            Integer id = reservation_.getReservationId();
            reservationMapper.setReservationSuccess(id);
            if(reservationService.getReservation(reservation_).getState().equals("reserved")){
                bookInfo.setIsbn(isbn);
                bookInfo.setState("canBorrow");
                List<BookInfo> bookInfos = getBookInfo(bookInfo);
                if(bookInfos.isEmpty()){
                    throw new Exception();
                }
                BookInfo bookInfo_ = bookInfos.get(0);
                bookInfo_.setState("reserved");
                updateBookInfo(bookInfo_);
                BookCatalog bookCatalog = bookCatalogService.getBookCatalogByIsbn(bookInfo.getIsbn());
                Integer readerId = reservation_.getReaderId();
                Reader reader_ = new Reader();
                reader_.setId(readerId);
                List<Reader> readers = userService.getReadeList(reader_);
                if(!readers.isEmpty()){
                    Reader reader = readers.get(0);
                    mailUtil.sendReservation(reader, bookCatalog, reservation_);
                    return;
                }
            }
        }
        bookCatalogService.setCanBorrow(1, isbn);
    }

    @Override
    public BookInfo getBookById(Integer id) {
        return bookInfoMapper.getBookInfoById(id);
    }
}
