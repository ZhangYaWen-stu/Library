package com.library.utilTest;

import com.library.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.lang.System.out;

@SpringBootTest
public class JwtTest {
    @Autowired
    JwtUtil jwtUtil;
    @Test
    void testJwt() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", "admin");
        claims.put("password", "123456");
        claims.put("role", "admin");
        String token = jwtUtil.getToken(claims);
        out.println(token);
        out.println(jwtUtil.parseToken(token).get("username").toString().equals("admin"));
        out.println(token);
    }
}
