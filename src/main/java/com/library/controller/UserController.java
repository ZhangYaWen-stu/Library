package com.library.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.library.pojo.Code;
import com.library.pojo.Librarian;
import com.library.pojo.Reader;
import com.library.pojo.Result;
import com.library.service.UserService;
import com.library.util.JwtUtil;
import com.library.util.LocalThread;
import com.library.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.spi.SyncResolver;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;


@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private MailUtil mailUtil;

    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> req){
        String username = req.get("username");
        String password = req.get("password");
        String token = userService.loginLibrarian(username, password);
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        if(token != null){
            Result result = Result.success(token);
            stringStringValueOperations.set(token,token);
            result.setCode(Code.ADMIN_USER);
            return result;
        }
        token = userService.loginReader(username, password);
        if(token != null){
            Result result = Result.success(token);
            stringStringValueOperations.set(token,token);
            result.setCode(Code.READER_USER);
            return result;
        }
        return Result.error("用户名或密码错误");
    }

    @PostMapping("/register")
    public Result register(@RequestBody Map<String, String> req)
    {
        String username = req.get("username");
        String password = req.get("password");
        if(userService.getReaderByName(username) != null){
            return Result.error("用户已存在");
        }
        Reader reader = new Reader();
        reader.setUserName(username);
        reader.setPassword(password);
        userService.addReader(reader);
        return Result.success();
    }

    @GetMapping("/userInfo")
    public Result userInfo()
    {
        Map<String, Object> claim = LocalThread.get();
        Integer id = Integer.parseInt(claim.get("id").toString());
        Reader reader = new Reader();
        reader.setId(id);
        Reader reader_ = userService.getReadeList(reader).get(0);
        return Result.success(reader_);
    }

    @PatchMapping("/updatePassword")
    public Result updatePassword(@RequestBody Map<String, String> req, @RequestHeader("Authorization") String token){
        String oldPassword = req.get("old_password");
        String newPassword = req.get("new_password");
        Map<String, Object> claim = LocalThread.get();
        String identity = claim.get("role").toString();
        String username = claim.get("username").toString();
        if (identity.equals("librarian")){
            Librarian librarian = new Librarian();
            librarian.setUserName(username);
            librarian.setPassword(oldPassword);
            if(userService.getLibrarian(librarian) == null){
                return Result.fail("旧密码错误");
            }
            String newToken = userService.updateLibrarianPassword(newPassword, token);
            return Result.success(newToken);
        }
        if(identity.equals("reader")){
            Reader reader = new Reader();
            reader.setUserName(username);
            reader.setPassword(oldPassword);
            if(userService.getReader(reader) == null){
                return Result.fail("旧密码错误");
            }
            String newToken = userService.updateReaderPassword(newPassword, token);
            return Result.success(newToken);
        }
        return Result.error();
    }
    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestPart("file") MultipartFile file) throws Exception {
        String newUrl = userService.updateAvatar(file);
        return Result.success(newUrl);
    }

    @PutMapping("/updateUserInfo")
    public Result updateUserInfo(@RequestBody Reader reader){
        Map<String, Object> claim = LocalThread.get();
        Integer id = Integer.parseInt(claim.get("id").toString());
        reader.setId(id);
        userService.updateReader(reader);
        return Result.success();
    }

    @GetMapping("/forgotPassword")
    public Result forgotPassword(String username, String email) throws Exception{
        Reader reader = userService.getReaderByName(username);

        if(reader == null) {
            return Result.error("未找到用户");
        }
        if(reader.getEmail() == null || !reader.getEmail().equals(email)){
            return Result.error("邮箱错误");
        }
        mailUtil.sendForgetPassword(reader);
        return Result.success();
    }

    @GetMapping("/admin/getAllReader")
    public Result getAllReader(Integer page, Integer pageSize){
        PageHelper.startPage((page - 1) * pageSize + 1, pageSize);
        List<Reader> reader = userService.getAllReader();
        return Result.success(reader);
    }

    @GetMapping("/admin/getReaderList")
    public Result getReaderList(Reader reader, Integer page, Integer pageSize){
        PageHelper.startPage((page - 1) * pageSize + 1, pageSize);
        List<Reader> readers = userService.getReadeList(reader);
        return Result.success(readers);
    }
    @DeleteMapping("/admin/deleteReader")
    public Result deleteReader(Integer id){
        userService.deleteReaderById(id);
        return Result.success();
    }
    @PatchMapping("/admin/updateReader")
    public Result updateReader(@RequestBody Reader reader){
        userService.updateReader(reader);
        return Result.success();
    }
}
