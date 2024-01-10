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

    @Update("update bookcatalog set CurrentCopies = CurrentCopies + ( #{num} ) where ISBN = #{isbn}")
    public void setCanBorrow(Integer num, String isbn);

    @Update("update bookcatalog set TotalCopies = TotalCopies + ( #{num} ) where ISBN = #{isbn}")
    public void setTotalBorrow(Integer num, String isbn);
    @Select("select * from bookcatalog")
    @ResultMap("bookCatalogMapper")
    public List<BookCatalog> getAllBookCatalog();

    @Delete("delete from bookcatalog where isbn = #{isbn}")
    public void deleteBookCatalogByIsbn(String isbn);

    @Select("select * from bookcatalog where isbn = #{isbn}")
    @ResultMap("bookCatalogMapper")
    public BookCatalog getBookCatalogByIsbn(String isbn);


}
