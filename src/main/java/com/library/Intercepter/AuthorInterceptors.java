package com.library.Intercepter;

import com.library.pojo.Result;
import com.library.util.LocalThread;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class AuthorInterceptors implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(request.getMethod().equals("OPTIONS")){
            return true;
        }

        Map<String, Object> claim = LocalThread.get();
        String identify = claim.get("role").toString();
        if(identify.equals("librarian")){
            return true;
        }
        response.setStatus(401);
        return false;
    }
}
