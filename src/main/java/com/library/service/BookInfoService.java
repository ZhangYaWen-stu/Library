package com.library.service;

import com.library.pojo.Book;
import com.library.pojo.BookInfo;


import java.util.List;

public interface BookInfoService {

    List<BookInfo> getBookInfo(BookInfo bookInfo);
    void updateBookInfo(BookInfo bookInfo) throws Exception;

    void addBookInfo(BookInfo bookInfo) throws Exception;

    void deleteBookInfo(BookInfo bookInfo);

    void reservationReminder(String isbn) throws Exception;

    BookInfo getBookById(Integer id);
    List<Book> getBookInfoAll(BookInfo bookInfo);

    public List<Book> getBookInfoAllByBook(Book book);
}
