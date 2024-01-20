package com.library.serviceTest;


import com.library.mapper.BorrowMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static java.lang.System.out;

@SpringBootTest
public class ReservationTest {
    @Autowired
    BorrowMapper borrowMapper;
    @Test
    void test(){
        out.println(borrowMapper.getBorrowOverdueWarn());
    }
}
