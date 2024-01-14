package com.library.controller;

import com.github.pagehelper.PageHelper;
import com.library.pojo.BookCatalog;
import com.library.pojo.Reservation;
import com.library.pojo.Result;
import com.library.service.BookCatalogService;
import com.library.util.LocalThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookCatalog")
public class BookCatalogController {
    @Autowired
    private BookCatalogService bookCatalogService;

    @PostMapping("/queryBookCatalog")
    public Result queryBookCatalog(@RequestBody BookCatalog bookCatalog, Integer page, Integer pageSize){
        PageHelper.startPage((page - 1) * pageSize + 1, pageSize);
        List<BookCatalog> bookCatalog_ = bookCatalogService.getBookCatalog(bookCatalog);
        return Result.success(bookCatalog_);
    }

    @PostMapping("/admin/queryBookCatalog")
    public Result adminQueryBookCatalog(@RequestBody BookCatalog bookCatalog, Integer page, Integer pageSize){
        PageHelper.startPage((page - 1) * pageSize + 1, pageSize);
        List<BookCatalog> bookCatalog_ = bookCatalogService.getBookCatalog(bookCatalog);
        return Result.success(bookCatalog_);
    }

    @PostMapping("/admin/addBookCatalog")
    public Result addBookCatalog(@RequestBody BookCatalog bookCatalog){
        Map<String, Object> claim = LocalThread.get();
        Integer id = Integer.parseInt(claim.get("id").toString());
        BookCatalog bookCatalog_ = new BookCatalog();
        bookCatalog_.setIsbn(bookCatalog.getIsbn());
        if(!bookCatalogService.getBookCatalog(bookCatalog_).isEmpty()){
            return Result.fail("该书目已存在");
        }
        bookCatalog_.setLibrarianJobNumber(id);
        bookCatalogService.addBookCatalog(bookCatalog);
        return Result.success();
    }
    @GetMapping("/admin/deleteBookCatalog")
    public Result deleteBookCatalog(BookCatalog bookCatalog){
        BookCatalog bookCatalog_ = new BookCatalog();
        bookCatalog_.setIsbn(bookCatalog.getIsbn());
        if(bookCatalogService.getBookCatalog(bookCatalog_).isEmpty()){
            return Result.fail("该书目不存在");
        }
        bookCatalogService.deleteBookCatalogByIsbn(bookCatalog.getIsbn());
        return Result.success();
    }

    @PostMapping("/admin/modifyBookCatalog")
    public Result modifyBookCatalog(@RequestBody BookCatalog bookCatalog){
        if(bookCatalogService.getBookCatalogByIsbn(bookCatalog.getIsbn()) == null){
            return Result.fail("该书目不存在");
        }
        bookCatalogService.updateBookCatalog(bookCatalog);
        return Result.success();
    }

    @PostMapping("/admin/updateBookCatalogCover")
    public Result updateBookCatalogCover(String isbn, @RequestPart("file") MultipartFile file) throws Exception {

        if(bookCatalogService.getBookCatalogByIsbn(isbn) == null){
            return Result.fail("该书目不存在");
        }
        String url_ = bookCatalogService.updateBookCatalogCover(isbn, file);
        Map<String, Object>url = new HashMap<>();
        url.put("url", url_);
        return Result.success(url);
    }


}
