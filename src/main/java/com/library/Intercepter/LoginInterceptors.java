package com.library.Intercepter;


import com.library.pojo.Result;
import com.library.util.JwtUtil;
import com.library.util.LocalThread;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoginInterceptors implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(request.getMethod().equals("OPTIONS")){
            return true;
        }

        String jwt = request.getHeader("Authorization");
        try{
            if (jwt == null) {
                throw new Exception();
            }
            ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
            String redisToken = stringStringValueOperations.get(jwt);
            if (redisToken == null){
                throw new Exception();
            }
            LocalThread.set(jwtUtil.parseToken(jwt));
        }
        catch (Exception e){
            response.setStatus(401);
            return false;
        }
        return true;
    }
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LocalThread.remove();
    }

}
