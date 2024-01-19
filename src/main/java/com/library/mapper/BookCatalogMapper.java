package com.library.mapper;

import com.library.pojo.BookCatalog;
import com.library.pojo.Librarian;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BookCatalogMapper {
    public List<BookCatalog> getBookCatalog(BookCatalog bookCatalog);

    public void addBookCatalog(BookCatalog bookCatalog);

    public void updateBookCatalog(BookCatalog bookCatalog);

    @Update("update book_catalog set can_borrow_number = can_borrow_number + ( #{num} ) where isbn = #{isbn}")
    public void setCanBorrow(Integer num, String isbn);

    @Update("update book_catalog set total_copies = total_copies + ( #{num} ) where isbn = #{isbn}")
    public void setTotalBorrow(Integer num, String isbn);
    @Select("select * from book_catalog")
    @ResultMap("bookCatalogMapper")
    public List<BookCatalog> getAllBookCatalog();

    @Delete("delete from book_catalog where isbn = #{isbn}")
    public void deleteBookCatalogByIsbn(String isbn);

    @Select("select * from book_catalog where isbn = #{isbn}")
    @ResultMap("bookCatalogMapper")
    public BookCatalog getBookCatalogByIsbn(String isbn);


}
