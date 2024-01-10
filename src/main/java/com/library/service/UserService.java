package com.library.service;

import com.library.pojo.Librarian;
import com.library.pojo.Reader;
import org.apache.tomcat.util.http.fileupload.MultipartStream;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.List;

@Service
public interface UserService {
    public void addReader(Reader reader);
    public Reader getReader(Reader reader);
    public Reader getReaderByName(String username);
    public List<Reader> getReadeList(Reader reader);
    public void updateReader(Reader reader);
    public void deleteReaderById(Integer id);
    public String updateReaderPassword(String password, String token);
    public String updateAvatar(MultipartFile file) throws Exception;
    public void updateBorrowNum(Integer borrowNum, Integer id);
    public List<Reader> getAllReader();
    public Librarian getLibrarian(Librarian librarian);
    public String updateLibrarianPassword(String password, String token);
    public String loginReader(String username, String password);
    public String loginLibrarian(String username, String password);
}
