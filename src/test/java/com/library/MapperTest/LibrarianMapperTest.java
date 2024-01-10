package com.library.MapperTest;

import com.library.mapper.LibrarianMapper;
import com.library.pojo.Librarian;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static java.lang.System.out;

@SpringBootTest
public class LibrarianMapperTest {
    @Autowired
    LibrarianMapper librarianMapper;
    @Test
    public void testGet(){
        Librarian librarian = new Librarian();
        librarian.setUserName("user123");
        librarian.setPassword("zlpassword123");
        Librarian librarian1 = librarianMapper.getLibrarian(librarian);
        out.println(librarian1);
    }
}
