package com.library.service;

import com.library.pojo.Borrow;
import com.library.pojo.BorrowList;
import com.library.pojo.Result;

import java.util.List;

public interface BorrowService {

    public Borrow getBorrow(Borrow borrow);
    public Result returnBook(Integer borrowId)throws Exception;
    public List<Borrow> getBorrowList(Borrow borrow);
    void deleteBorrowList(Borrow borrow) throws Exception;
    void updateBorrowList(Borrow borrow) throws Exception;
    void checkBorrowOverdue()throws Exception;
    void checkBorrowOverdueWarn()throws Exception;
    void reservationRemind(String isbn)throws Exception;

    void addBorrow(Borrow borrow);

    List<BorrowList> getBorrowListAll(Borrow borrow);
}
