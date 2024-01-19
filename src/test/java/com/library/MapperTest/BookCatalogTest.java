package com.library.MapperTest;

import com.library.mapper.BookCatalogMapper;
import com.library.pojo.BookCatalog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static java.lang.System.out;

@SpringBootTest
public class BookCatalogTest {
    @Autowired
    BookCatalogMapper bookCatalogMapper;
    @Test
    public void testGet(){
        List<BookCatalog> bookCatalog = bookCatalogMapper.getAllBookCatalog();
        out.println(bookCatalog);
        BookCatalog bookCatalog1 = new BookCatalog();
        bookCatalog1.setIsbn("9787302329989");
        bookCatalog1.setBookName("数据结构");
        bookCatalog1.setAuthor("严蔚敏");
        bookCatalog1.setPublisher("清华大学出版社");
        bookCatalog1.setPublishDate(new Date());
        bookCatalog1.setUnitPrice(49.00f);
        bookCatalog1.setLibrarianJobNumber(1);
        out.println(bookCatalogMapper.getBookCatalog(bookCatalog1));
    }
}
