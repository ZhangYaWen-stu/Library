package com.library.service.Implement;

import com.library.mapper.LibrarianMapper;
import com.library.mapper.ReaderMapper;
import com.library.pojo.Librarian;
import com.library.pojo.Reader;
import com.library.pojo.Result;
import com.library.service.UserService;
import com.library.util.AliOssUtil;
import com.library.util.JwtUtil;
import com.library.util.LocalThread;
import org.apache.tomcat.util.http.fileupload.MultipartStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private ReaderMapper readerMapper;
    @Autowired
    private LibrarianMapper librarianMapper;
    @Autowired
    private AliOssUtil aliOssUtil;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Reader getReaderByName(String username) {
        return readerMapper.getReaderByUsername(username);
    }

    @Override
    public void addReader(Reader reader) {
        readerMapper.addReader(reader);
    }

    @Override
    public Reader getReader(Reader reader) {
        return readerMapper.getReader(reader);
    }
    @Override
    public List<Reader> getReadeList(Reader reader) {
        return readerMapper.getReaderList(reader);
    }
    @Override
    public void updateReader(Reader reader) {
        readerMapper.updateReader(reader);
    }

    @Override
    public void deleteReaderById(Integer id) {
        readerMapper.deleteReaderById(id);
    }

    @Override
    public String updateReaderPassword(String password, String token) {
        Map<String, Object>claims = LocalThread.get();
        Reader reader = new Reader();
        reader.setId(Integer.parseInt(claims.get("id").toString()));
        reader.setPassword(password);
        readerMapper.updateReader(reader);
        jwtUtil.getToken(claims);
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        stringStringValueOperations.getOperations().delete(token);
        String newToken = jwtUtil.getToken(claims);
        stringStringValueOperations.set(newToken, newToken);
        return newToken;
    }

    @Override
    public String updateAvatar(MultipartFile file) throws Exception {
        String objectName = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        objectName = UUID.randomUUID() + objectName.substring(objectName.lastIndexOf("."));
        String avatar = aliOssUtil.uploadFile(objectName, inputStream);
        Map<String, Object> claims = LocalThread.get();
        Reader reader = new Reader();
        reader.setId(Integer.parseInt(claims.get("id").toString()));
        reader.setAvatar(avatar);
        readerMapper.updateReader(reader);
        return avatar;
    }

    @Override
    public void updateBorrowNum(Integer borrowNum, Integer id) {
        readerMapper.setBorrowNum(borrowNum, id);
    }

    @Override
    public List<Reader> getAllReader() {
        return readerMapper.getAllReader();
    }

    @Override
    public Librarian getLibrarian(Librarian librarian) {
        return librarianMapper.getLibrarian(librarian);
    }

    @Override
    public String updateLibrarianPassword(String password, String token) {
        Map<String, Object>claims = LocalThread.get();
        librarianMapper.updateLibrarianPassword(password, Integer.parseInt(claims.get("id").toString()));
        String newToken = jwtUtil.getToken(claims);
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        stringStringValueOperations.getOperations().delete(token);
        stringStringValueOperations.set(newToken, newToken);
        return newToken;
    }

    @Override
    public String loginReader(String username, String password) {
        Reader reader = new Reader();
        reader.setUserName(username);
        reader.setPassword(password);
        Reader reader_ = getReader(reader);
        if (reader_ == null) {
            return null;
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", reader_.getId().toString());
        claims.put("username", reader_.getUserName());
        claims.put("role", "reader");
        return jwtUtil.getToken(claims);
    }

    @Override
    public String loginLibrarian(String username, String password) {
        Librarian librarian = new Librarian();
        librarian.setUserName(username);
        librarian.setPassword(password);
        Librarian librarian_ = getLibrarian(librarian);
        if (librarian_ == null) {
            return null;
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", librarian_.getJobNumber().toString());
        claims.put("username", librarian_.getUserName());
        claims.put("role", "librarian");
        return jwtUtil.getToken(claims);
    }
}
