package com.library.pojoTest;

import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.Date;

import static java.lang.System.out;

@SpringBootTest
public class DataTest {

    @Test
    public void testDate(){
        out.println(new Date());
    }
}
