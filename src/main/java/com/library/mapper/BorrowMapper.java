package com.library.mapper;

import com.library.pojo.Borrow;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface BorrowMapper {
    Borrow getBorrow(Borrow borrow);
    @Update("update borrowing set state = 'returned' , ReturnTime = NOW() where BorrowID = #{borrowId} and (state = 'normal' or state = 'warned')")
    void setReturnBook(Integer borrowId);

    List<Borrow> getBorrowList(Borrow borrow);

    void deleteBorrowList(Borrow borrow);

    void updateBorrowList(Borrow borrow);

    @Update("update borrowing set state = 'warned' where state = 'normal' and BorrowID = #{borrowId}")
    void setWarnBorrow(Integer borrowId);

    @Update("update borrowing set state = 'overdue' where state = 'warned' and BorrowID = #{borrowId}")
    void setOverdueBorrow(Integer borrowId);

    @Select("select * from borrowing where NOW() > DueTime and state = 'warned'")
    @ResultMap("borrowMap")
    List<Borrow> getBorrowOverdue();

    @Select("select * from borrowing where NOW() > (DueTime - INTERVAL 1 DAY) and state = 'normal'")
    @ResultMap("borrowMap")
    List<Borrow> getBorrowOverdueWarn();

    @Insert("INSERT into borrowing(ReaderID, BookID, BorrowTime, DueTime, ReturnTime, LGH, state) values (#{readerId}, #{bookId}, #{borrowTime}, #{dueTime}, #{returnTime}, #{librarianJobNumber}, #{state})")
    void addBorrow(Borrow borrow);
}
