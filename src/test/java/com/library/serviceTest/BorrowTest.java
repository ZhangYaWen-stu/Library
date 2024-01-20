package com.library.serviceTest;

import com.library.mapper.BorrowMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BorrowTest {
    @Autowired
    BorrowMapper borrowMapper;
    @Test
    void test(){
        System.out.println(borrowMapper.getBorrowOverdueWarn());
    }
}
