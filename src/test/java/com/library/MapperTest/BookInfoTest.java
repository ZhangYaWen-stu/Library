package com.library.MapperTest;


import com.library.mapper.BookInfoMapper;
import com.library.pojo.BookInfo;
import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BookInfoTest {
    @Autowired
    BookInfoMapper bookInfoMapper;

    @Test
    public void bookInfoAll(){
        BookInfo bookInfo = new BookInfo();
        bookInfo.setId(1);
        bookInfoMapper.getBookInfoAll(bookInfo);

    }
}
