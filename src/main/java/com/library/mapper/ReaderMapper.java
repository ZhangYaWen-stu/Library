package com.library.mapper;

import com.library.pojo.Reader;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReaderMapper {

    @ResultMap("readerMap")
    @Select("select * from reader where username = #{userName} and password = #{password}")
    Reader getReader(Reader reader);

    @ResultMap("readerMap")
    @Select("select * from reader where username = #{username}")
    Reader getReaderByUsername(String username);
    @ResultMap("readerMap")
    List<Reader> getReaderList(Reader reader);

    @Insert("insert into reader(username,password,email, telephone) " +
            "values(#{userName},#{password}, #{email}, #{telephoneNumber})")
    void addReader(Reader reader);
    @Delete("delete from reader where id = #{id}")
    void deleteReaderById(Integer id);

    void updateReader(Reader reader);

    @Update("update reader set borrow_number = borrow_number + (#{num}) where id = #{id}")
    void setBorrowNum(Integer num, Integer id);

    @ResultMap("readerMap")
    @Select("select * from reader")
    List<Reader> getAllReader();

    @Select("select * from reader where id = #{id}")
    @ResultMap("readerMap")
    Reader getReaderById(Integer id);
}
