package com.library.mapper;

import com.library.pojo.Book;
import com.library.pojo.BookInfo;
import com.library.pojo.Borrow;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BookInfoMapper {

    List<BookInfo> getBookInfos(BookInfo bookInfo);

    void updateBookInfo(BookInfo bookInfo);

    @Insert("insert into book_info(isbn, location, state, librarian_job_number) values (#{isbn}, #{location}, #{state}, #{librarianJobNumber})")
    void addBookInfo(BookInfo bookInfo);

    void deleteBookInfo(BookInfo bookInfo);

    @Select("select * from book_info where id = #{id}")
    BookInfo getBookInfoById(Integer id);

    List<Book> getBookInfoAll(BookInfo bookInfo);

    List<Book> getBookInfoAllByBook(Book book);
}
