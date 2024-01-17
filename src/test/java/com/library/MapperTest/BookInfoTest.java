package com.library.MapperTest;


import com.library.mapper.BookInfoMapper;
import com.library.pojo.BookInfo;
import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static java.lang.System.out;

@SpringBootTest
public class BookInfoTest {
    @Autowired
    BookInfoMapper bookInfoMapper;

    @Test
    public void bookInfoAll(){

        BookInfo bookInfo = new BookInfo();
        bookInfo.setIsbn("1212121212121");
        out.println(bookInfoMapper.getBookInfoAll(bookInfo));

    }
}
