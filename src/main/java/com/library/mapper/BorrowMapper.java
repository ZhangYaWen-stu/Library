package com.library.mapper;

import com.library.pojo.Borrow;
import com.library.pojo.BorrowList;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface BorrowMapper {
    Borrow getBorrow(Borrow borrow);
    @Update("update borrowing set state = 'returned' , return_time = NOW() where borrowId = #{borrowId} and (state = 'normal' or state = 'warned')")
    void setReturnBook(Integer borrowId);

    List<Borrow> getBorrowList(Borrow borrow);

    void deleteBorrowList(Borrow borrow);

    void updateBorrowList(Borrow borrow);

    @Update("update borrowing set state = 'warned' where state = 'normal' and borrowId = #{borrowId}")
    void setWarnBorrow(Integer borrowId);

    @Update("update borrowing set state = 'overdue' where state = 'warned' and borrowId = #{borrowId}")
    void setOverdueBorrow(Integer borrowId);

    @Select("select * from borrowing where NOW() > due_time and state = 'warned'")
    @ResultMap("borrowMap")
    List<Borrow> getBorrowOverdue();

    @Select("select * from borrowing where NOW() > (due_time - INTERVAL 1 DAY) and state = 'normal'")
    @ResultMap("borrowMap")
    List<Borrow> getBorrowOverdueWarn();

    @Insert("INSERT into borrowing(readerId, bookId, borrow_time, due_time, borrowId, librarian_job_number, state) values" +
            " (#{readerId}, #{bookId}, #{borrowTime}, #{dueTime}, #{borrowId}, #{librarianJobNumber}, #{state})")
    void addBorrow(Borrow borrow);

    List<BorrowList> getBorrowListAll(Borrow borrow);
}
