package com.library.pojoTest;


import com.library.LibraryApplication;
import com.library.pojo.Code;
import com.library.pojo.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static java.lang.System.out;

@SpringBootTest
public class ResultTest {

    @Test
    public void testToString() {
        Result x = new Result();
        x.setCode(Code.SUCCESS);
        x.setMsg("success");
        x.setData(null);
        out.println(x);
    }


}
