package com.library.MapperTest;

import com.library.mapper.BookCatalogMapper;
import com.library.pojo.BookCatalog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    }
}
