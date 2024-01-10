package com.library.serviceTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static java.lang.System.out;

@SpringBootTest
public class UserServiceTest {

    @Test
    public void testToString(){
        String tes = "21313";
        out.println(tes.toString());
    }
}
