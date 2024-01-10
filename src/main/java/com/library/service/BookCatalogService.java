package com.library.service;

import com.library.pojo.BookCatalog;
import com.library.pojo.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface BookCatalogService {
    public List<BookCatalog> getAllBookCatalog();
    public void addBookCatalog(BookCatalog bookCatalog);
    public void updateBookCatalog(BookCatalog bookCatalog);
    public void deleteBookCatalogByIsbn(String isbn);
    public List<BookCatalog> getBookCatalog(BookCatalog bookCatalog);
    public BookCatalog getBookCatalogByIsbn(String isbn);
    public void setCanBorrow(Integer num, String isbn);
    void setTotalBorrow(Integer num, String isbn);

    String updateBookCatalogCover(String isbn, MultipartFile file) throws Exception;
}
