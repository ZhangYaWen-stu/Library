package com.library.controller;

import com.github.pagehelper.PageHelper;
import com.library.pojo.BookInfo;
import com.library.pojo.Borrow;
import com.library.pojo.Reader;
import com.library.pojo.Result;
import com.library.service.BookCatalogService;
import com.library.service.BookInfoService;
import com.library.service.BorrowService;
import com.library.service.UserService;
import com.library.util.LocalThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookBorrow")
public class BookBorrowController {

    @Autowired
    BorrowService borrowService;
    @Autowired
    BookInfoService bookInfoService;
    @Autowired
    BookCatalogService bookCatalogService;
    @Autowired
    UserService userService;

    @PatchMapping("/returnBook")
    public Result returnBook(Integer borrowId)throws Exception{
        return borrowService.returnBook(borrowId);
    }

    @PutMapping("/borrowBook")
    public Result borrowBook(@RequestBody Borrow borrow, String isbn, Integer borrowNum) throws Exception{
        Map<String, Object> claim = LocalThread.get();
        Integer id = Integer.parseInt(claim.get("id").toString());
        borrow.setReaderId(id);
        if(borrow.getBookId() == null){
            BookInfo bookInfo = new BookInfo();
            bookInfo.setState("canBorrow");
            bookInfo.setIsbn(isbn);
            if(bookCatalogService.getBookCatalogByIsbn(isbn) == null){
                return Result.fail("无本种数目");
            }
            if(bookCatalogService.getBookCatalogByIsbn(isbn).getCanBorrow() < borrowNum){
                return Result.fail("该书已无足够剩余书籍");
            }
            List<BookInfo> bookInfos = bookInfoService.getBookInfo(bookInfo);
            for(int i = 0; i < borrowNum; i++){
                BookInfo bookInfo_ = bookInfos.get(i);
                bookInfo_.setState("borrowed");
                bookInfoService.updateBookInfo(bookInfo_);
                borrow.setState("normal");
                borrow.setBookId(bookInfo_.getId());
                borrow.setBorrowTime(new Date());
                borrowService.addBorrow(borrow);
                bookCatalogService.setCanBorrow(-1, bookInfo_.getIsbn());
                userService.updateBorrowNum(1, id);

            }
            return Result.success();
        }
        else {
            BookInfo bookInfo = new BookInfo();
            bookInfo.setState("canBorrow");
            bookInfo.setId(borrow.getBookId());
            List<BookInfo> bookInfos = bookInfoService.getBookInfo(bookInfo);
            if(!bookInfos.isEmpty()){
                userService.updateBorrowNum(1, id);
                bookCatalogService.setCanBorrow(-1, isbn);
                bookInfo = bookInfos.get(0);
                bookInfo.setState("borrowed");
                bookInfoService.updateBookInfo(bookInfo);
                borrow.setState("normal");
                borrow.setBookId(bookInfo.getId());
                borrow.setBorrowTime(new Date());
                borrowService.addBorrow(borrow);
                bookCatalogService.setCanBorrow(-1, bookInfo.getIsbn());
                return Result.success();
            }
        }
        return Result.fail();
    }

    @PostMapping("/getBorrowBookList")
    public Result getBorrowBookList(@RequestBody Borrow borrow, Integer page, Integer pageSize) {
        Map<String, Object> claim = LocalThread.get();
        Integer id = Integer.parseInt(claim.get("id").toString());
        borrow.setReaderId(id);
        PageHelper.startPage((page - 1) * pageSize + 1, pageSize);
        List<Borrow> borrowList = borrowService.getBorrowList(borrow);
        return Result.success(borrowList);
    }
    @PostMapping("/admin/getBorrowBookList")
    public Result getBorrowBookListAdmin(@RequestBody Borrow borrow, Integer page, Integer pageSize) {
        PageHelper.startPage((page - 1) * pageSize + 1, pageSize);
        List<Borrow> borrowList = borrowService.getBorrowList(borrow);
        return Result.success(borrowList);
    }

    @PostMapping("/admin/updateBorrowBookList")
    public Result updateBorrowBookList(@RequestBody Borrow borrow) throws Exception {
        if(borrow.getReaderId() != null)
            return Result.fail("不可修改读者id");
        borrowService.updateBorrowList(borrow);
        return Result.success();
    }

    @PostMapping("/admin/deleteBorrowBookList")
    public Result deleteBorrowBookList(@RequestBody Borrow borrow) throws Exception {
        borrowService.deleteBorrowList(borrow);
        return Result.success();
    }
}
