package com.library.service.Implement;

import com.library.mapper.BookInfoMapper;
import com.library.mapper.BorrowMapper;
import com.library.mapper.ReaderMapper;
import com.library.mapper.ReservationMapper;
import com.library.pojo.*;
import com.library.service.*;
import com.library.util.LocalThread;
import com.library.util.MailUtil;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.BreakIterator;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class BorrowServiceImpl implements BorrowService {
    @Autowired
    BorrowMapper borrowMapper;
    @Autowired
    BookCatalogService bookCatalogService;
    @Autowired
    UserService userService;
    @Autowired
    BookInfoMapper bookInfoMapper;
    @Autowired
    MailUtil mailUtil;
    @Autowired
    ReservationMapper reservationMapper;
    @Autowired
    ReaderMapper readerMapper;
    @Override
    public Borrow getBorrow(Borrow borrow) {
        return borrowMapper.getBorrow(borrow);
    }

    @Override
    public Result returnBook(Integer borrowId) throws Exception{
        Map<String, Object> claim = LocalThread.get();
        Integer id =Integer.parseInt(claim.get("id").toString());
        Borrow borrow = new Borrow();
        borrow.setReaderId(id);
        borrow.setBorrowId(borrowId);
        if(getBorrow(borrow) == null){
            return Result.fail("该借书记录不存在");
        }
        if(getBorrow(borrow).getState().equals("returned") || getBorrow(borrow).getState().equals("overdue")){
            return Result.fail("该借书记录异常");
        }
        borrowMapper.setReturnBook(borrowId);
        Borrow borrow_ = getBorrow(borrow);
        if(borrow_.getState().equals("returned")){
            borrow_.setReturnTime(new Date());
            borrowMapper.updateBorrowList(borrow_);
            Integer bookId = borrow_.getBookId();
            BookInfo bookInfo = new BookInfo();
            bookInfo.setId(bookId);
            String isbn = bookInfoMapper.getBookInfos(bookInfo).get(0).getIsbn();
            bookInfo.setState("canBorrow");
            bookInfoMapper.updateBookInfo(bookInfo);
            reservationRemind(isbn);
            readerMapper.setBorrowNum(-1, id);
            return Result.success();
        }else {
            borrow_.setReturnTime(new Date());
            borrowMapper.updateBorrowList(borrow_);
            Result result = Result.fail("该借书记录异常");
            String isbn = bookInfoMapper.getBookInfoById(borrow_.getBookId()).getIsbn();
            Float bookPrice_ = bookCatalogService.getBookCatalogByIsbn(isbn).getUnitPrice();
            Float unitPrice = 1f;
            if(bookPrice_ != null){
                unitPrice = bookPrice_;
            }
            result.setData(ChronoUnit.DAYS.between(borrow_.getDueTime().toInstant(), new Date().toInstant()) * unitPrice);
            return result;
        }
    }

    @Override
    public List<Borrow> getBorrowList(Borrow borrow) {
        return borrowMapper.getBorrowList(borrow);
    }

    @Override
    public void deleteBorrowList(Borrow borrow) throws Exception {
        List<Borrow> borrows = borrowMapper.getBorrowList(borrow);
        for(Borrow borrow_ : borrows){
            borrowMapper.deleteBorrowList(borrow_);
            if(borrow_.getState().equals("normal") || borrow_.getState().equals("overdue")){
                Integer readerId = borrow_.getReaderId();
                readerMapper.setBorrowNum(-1, readerId);
                Integer bookId = borrow_.getBookId();
                BookInfo bookInfo_ = bookInfoMapper.getBookInfoById(bookId);
                bookInfo_.setState("canBorrow");
                bookInfoMapper.updateBookInfo(bookInfo_);
                reservationRemind(bookInfo_.getIsbn());
                bookInfo_ = bookInfoMapper.getBookInfoById(bookId);
//                if(bookInfo_.getState().equals("canBorrow")){
//                    bookCatalogService.setCanBorrow(-1, bookInfo_.getIsbn());
//                }
            }
        }
    }

    @Override
    public void updateBorrowList(Borrow borrow) throws Exception {
        borrowMapper.updateBorrowList(borrow);
        List<Borrow> borrows = borrowMapper.getBorrowList(borrow);
        for(Borrow borrow_ : borrows){
            borrow.setBookId(borrow_.getBookId());
            if(borrow.getState() != null){
                if((borrow.getState().equals("overdue") || (borrow.getState().equals("normal")) && borrow_.getState().equals("returned"))){
                    continue;
                }
                borrowMapper.updateBorrowList(borrow);
                if((borrow_.getState().equals("overdue") || (borrow_.getState().equals("normal")) && ((borrow.getState().equals("returned") || borrow.getState().equals("warned"))))){
                    Integer readerId = borrow_.getReaderId();
                    readerMapper.setBorrowNum(-1, readerId);
                    Integer bookId = borrow_.getBookId();
                    BookInfo bookInfo = bookInfoMapper.getBookInfoById(bookId);
                    bookInfo.setState("canBorrow");
                    bookInfoMapper.updateBookInfo(bookInfo);
                    reservationRemind(bookInfo.getIsbn());
                }

            }
        }
    }

    @Override
    @Scheduled(fixedRate = 10 * 1000)
    public void checkBorrowOverdue() throws Exception{
        List<Borrow> borrows = borrowMapper.getBorrowOverdue();
        for(Borrow borrow_ : borrows){
            Borrow borrow = new Borrow();
            borrow.setBorrowId(borrow_.getBorrowId());
            borrowMapper.setOverdueBorrow(borrow_.getBorrowId());
            if(getBorrow(borrow).getState().equals("overdue")){
                borrow_ = getBorrow(borrow);
                Integer readerId = borrow.getReaderId();
                Reader reader_ = new Reader();
                reader_.setId(readerId);
                Reader reader = userService.getReaderById(readerId);
                BookInfo bookInfo_ = new BookInfo();
                bookInfo_.setId(borrow_.getBookId());
                BookInfo bookInfo = bookInfoMapper.getBookInfos(bookInfo_).get(0);
                BookCatalog bookCatalog = bookCatalogService.getBookCatalogByIsbn(bookInfo.getIsbn());
                mailUtil.sendBorrowOverdue(reader, bookCatalog, borrow_);
            }
        }
    }

    @Override
    @Scheduled(fixedRate = 10 * 1000)
    public void checkBorrowOverdueWarn() throws Exception{
        List<Borrow> borrows = borrowMapper.getBorrowOverdueWarn();
        for(Borrow borrow_ : borrows){
            Borrow borrow = new Borrow();
            borrow.setBorrowId(borrow_.getBorrowId());
            borrowMapper.setWarnBorrow(borrow_.getBorrowId());
            if(getBorrow(borrow).getState().equals("warned")){
                borrow_ = getBorrow(borrow);
                Integer readerId = borrow.getReaderId();
                Reader reader = userService.getReaderById(readerId);
                BookInfo bookInfo_ = new BookInfo();
                bookInfo_.setId(borrow_.getBookId());
                BookInfo bookInfo = bookInfoMapper.getBookInfos(bookInfo_).get(0);
                BookCatalog bookCatalog = bookCatalogService.getBookCatalogByIsbn(bookInfo.getIsbn());
                mailUtil.sendBorrowOverdueWarn(reader, bookCatalog, borrow_);
            }
        }
    }

    @Override
    public void reservationRemind(String isbn) throws Exception{
        Reservation reservation = new Reservation();
        reservation.setIsbn(isbn);
        reservation.setState("reserving");
        BookInfo bookInfo = new BookInfo();
        List<Reservation> reservations = reservationMapper.getReservationList(reservation);
        for(Reservation reservation_ : reservations){
            Integer id = reservation_.getReservationId();
            reservationMapper.setReservationSuccess(id);
            if(reservationMapper.getReservation(reservation_).getState().equals("reserved")){
                bookInfo.setIsbn(isbn);
                bookInfo.setState("canBorrow");
                List<BookInfo> bookInfos = bookInfoMapper.getBookInfos(bookInfo);
                if(bookInfos.isEmpty()){
                    throw new Exception();
                }
                BookInfo bookInfo_ = bookInfos.get(0);
                bookInfo_.setState("reserved");
                bookInfoMapper.updateBookInfo(bookInfo_);
                BookCatalog bookCatalog = bookCatalogService.getBookCatalogByIsbn(bookInfo.getIsbn());
                Integer readerId = reservation_.getReaderId();
                Reader reader = userService.getReaderById(readerId);
                mailUtil.sendReservation(reader, bookCatalog, reservation_);
                return;
            }
        }
        bookCatalogService.setCanBorrow(1, isbn);
    }

    @Override
    public void addBorrow(Borrow borrow) {
        borrowMapper.addBorrow(borrow);
    }

    @Override
    public List<BorrowList> getBorrowListAll(Borrow borrow) {
        return borrowMapper.getBorrowListAll(borrow);
    }
}
