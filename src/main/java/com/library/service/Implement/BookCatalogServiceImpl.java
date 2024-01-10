package com.library.service.Implement;

import com.library.mapper.BookCatalogMapper;
import com.library.pojo.BookCatalog;
import com.library.pojo.Reader;
import com.library.pojo.Result;
import com.library.service.BookCatalogService;
import com.library.util.AliOssUtil;
import com.library.util.LocalThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Book;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BookCatalogServiceImpl implements BookCatalogService {

    @Autowired
    BookCatalogMapper bookCatalogMapper;
    @Autowired
    AliOssUtil aliOssUtil;
    @Override
    public List<BookCatalog> getBookCatalog(BookCatalog bookCatalog){
        if (bookCatalog.getIsbn() != null){
            bookCatalog.setIsbn("%" + bookCatalog.getIsbn() + "%");
        }
        if (bookCatalog.getBookName() != null){
            bookCatalog.setBookName("%" + bookCatalog.getBookName() + "%");
        }
        if (bookCatalog.getAuthor() != null){
            StringBuilder author = new StringBuilder("%");
            String[] authors = bookCatalog.getAuthor().split(" ");
            for(String x : authors){
                if (x.isEmpty()) continue;
                author.append(x).append(" ");
            }
            String author_ = author.toString().trim();
            bookCatalog.setAuthor(author_ + "%");
        }
        if (bookCatalog.getPublisher() != null){
            bookCatalog.setPublisher("%" + bookCatalog.getPublisher() + "%");
        }
        return bookCatalogMapper.getBookCatalog(bookCatalog);
    }

    @Override
    public void addBookCatalog(BookCatalog bookCatalog) {
        bookCatalogMapper.addBookCatalog(bookCatalog);
    }

    @Override
    public void updateBookCatalog(BookCatalog bookCatalog){
        bookCatalogMapper.updateBookCatalog(bookCatalog);
    }

    @Override
    public List<BookCatalog> getAllBookCatalog() {
        return bookCatalogMapper.getAllBookCatalog();
    }

    @Override
    public void deleteBookCatalogByIsbn(String isbn) {
        bookCatalogMapper.deleteBookCatalogByIsbn(isbn);
    }

    @Override
    public BookCatalog getBookCatalogByIsbn(String isbn) {
        return bookCatalogMapper.getBookCatalogByIsbn(isbn);
    }

    @Override
    public void setCanBorrow(Integer num, String isbn) {
        bookCatalogMapper.setCanBorrow(num, isbn);
    }

    @Override
    public void setTotalBorrow(Integer num, String isbn) {
        bookCatalogMapper.setTotalBorrow(num, isbn);
    }

    @Override
    public String updateBookCatalogCover(String isbn, MultipartFile file) throws Exception {
        String objectName = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        objectName = UUID.randomUUID() + objectName.substring(objectName.lastIndexOf("."));
        String avatar = aliOssUtil.uploadFile(objectName, inputStream);
        BookCatalog bookCatalog = new BookCatalog();
        bookCatalog.setIsbn(isbn);
        bookCatalog.setCover(avatar);
        bookCatalogMapper.updateBookCatalog(bookCatalog);
        return avatar;
    }
}
