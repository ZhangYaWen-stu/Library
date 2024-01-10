package com.library.MapperTest;

import com.library.mapper.ReaderMapper;
import com.library.pojo.Reader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static java.lang.System.out;

@SpringBootTest
public class ReaderMapperTest {
    @Autowired
    ReaderMapper readerMapper;

    @Test
    public void testGetReader(){
//        Reader reader = new Reader();
//        reader.setUserName("umu123");
//        Reader reader_ = readerMapper.getReader(reader);
        List<Reader> reader = readerMapper.getAllReader();
        out.println(reader);
    }
}
