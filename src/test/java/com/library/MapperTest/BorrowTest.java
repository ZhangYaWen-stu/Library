package com.library.MapperTest;

import com.library.mapper.BorrowMapper;
import com.library.pojo.Borrow;
import com.library.pojo.BorrowList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BorrowTest {
    @Autowired
    BorrowMapper borrowMapper;


    @Test
    public void borrowListTest(){
        Borrow borrow = new Borrow();
        borrow.setBorrowId(1);
        borrowMapper.getBorrowListAll(borrow);
    }
}
