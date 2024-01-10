package com.library.serviceTest;

import com.library.service.BookCatalogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class BookCatalogTest {
    @Test
    void testStringSplit() {
        String s = "123  456    789";
        String[] ss = s.split(" ");
        StringBuilder author = new StringBuilder("%");
        for (String x : ss) {
            if (x.isEmpty()) continue;
            author.append(x).append(" ");
            System.out.println(x);
        }
        String author_ = author.toString().trim();
        System.out.println(author_+ "%");
    }
}
