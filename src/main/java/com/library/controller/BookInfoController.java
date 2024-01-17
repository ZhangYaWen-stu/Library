package com.library.controller;

import com.github.pagehelper.PageHelper;
import com.library.pojo.BookInfo;
import com.library.pojo.Result;
import com.library.pojo.Book;
import com.library.service.BookCatalogService;
import com.library.service.BookInfoService;
import com.library.service.BorrowService;
import com.library.service.ReservationService;
import com.library.util.LocalThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/bookInfo")
public class BookInfoController {
    @Autowired
    BookInfoService bookInfoService;
    @Autowired
    BookCatalogService bookCatalogService;
    @Autowired
    BorrowService borrowService;

    @PostMapping("/getBook")
    public Result getBook(Integer page, Integer pageSize, @RequestBody BookInfo bookInfo){
        bookInfo.setLibrarianJobNumber(null);
        PageHelper.startPage((page - 1) * pageSize + 1, pageSize);
        List<Book> bookInfos = bookInfoService.getBookInfoAll(bookInfo);
        for(int i = 0; i < bookInfos.size() ; i++){
            bookInfos.get(0).setLibrarianJobNumber(null);
        }
        return Result.success(bookInfos);
    }

    @PostMapping("/admin/getBookInfo")
    public Result getBookInfo(Integer page, Integer pageSize, @RequestBody BookInfo bookInfo){
        PageHelper.startPage((page - 1) * pageSize + 1, pageSize);
        List<Book> bookInfos = bookInfoService.getBookInfoAll(bookInfo);
        return Result.success(bookInfos);
    }

    @PostMapping("/admin/updateBookInfo")
    public Result updateBookInfo(@RequestBody BookInfo bookInfo) throws Exception{
        BookInfo bookInfo_ = bookInfoService.getBookById(bookInfo.getId());
        if(bookInfo_ == null){
            return Result.fail("该书目不存在");
        }
        if(bookInfo.getLocation() == null && bookInfo.getLibrarianJobNumber() == null){
            return Result.fail("参数错误");
        }
        if(!bookInfo_.getState().equals("canBorrow") && !bookInfo_.getState().equals("cantBorrow")){
            return Result.fail("处于被预约或已借出状态的书目不可修改");
        }
        bookInfoService.updateBookInfo(bookInfo);
        return Result.success();
    }

    @PostMapping("/admin/addBookInfo")
    public Result addBookInfo(@RequestBody BookInfo bookInfo) throws Exception {
        Map<String, Object> claim = LocalThread.get();
        Integer id = Integer.parseInt(claim.get("id").toString());
        bookInfo.setLibrarianJobNumber(id);
        if(bookCatalogService.getBookCatalogByIsbn(bookInfo.getIsbn()) == null){
            return Result.fail("该书目不存在");
        }
        bookInfoService.addBookInfo(bookInfo);
        return Result.success();
    }

    @PostMapping("/admin/deleteBookInfo")
    public Result deleteBookInfo(@RequestBody BookInfo bookInfo){
        List<BookInfo> bookInfos = bookInfoService.getBookInfo(bookInfo);
        if(bookInfos.isEmpty()){
            return Result.fail("该书目不存在");
        }
        for (BookInfo bookInfo_ : bookInfos)
        {
            BookInfo bookInfo_t = bookInfoService.getBookById(bookInfo_.getId());
            if (!bookInfo_t.getState().equals("canBorrow") && !bookInfo_t.getState().equals("cantBorrow")) {
                return Result.fail("处于被预约或已借出状态的书目不可修改");
            }
            bookInfoService.deleteBookInfo(bookInfo_t);
        }
        return Result.success();
    }
}
