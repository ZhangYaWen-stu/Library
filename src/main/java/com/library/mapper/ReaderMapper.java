package com.library.mapper;

import com.library.pojo.Reader;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReaderMapper {

    @ResultMap("readerMap")
    @Select("select * from reader where Username = #{userName} and Password = #{password}")
    Reader getReader(Reader reader);

    @ResultMap("readerMap")
    @Select("select * from reader where Username = #{username}")
    Reader getReaderByUsername(String username);
    @ResultMap("readerMap")
    List<Reader> getReaderList(Reader reader);

    @Insert("insert into reader(Username,Password) " +
            "values(#{userName},#{password})")
    void addReader(Reader reader);
    @Delete("delete from reader where ID = #{id}")
    void deleteReaderById(Integer id);

    void updateReader(Reader reader);

    @Update("update reader set BorrNum = BorrNum + (#{num}) where ID = #{id}")
    void setBorrowNum(Integer num, Integer id);

    @ResultMap("readerMap")
    @Select("select * from reader")
    List<Reader> getAllReader();

    @Select("select * from reader where ID = #{id}")
    @ResultMap("readerMap")
    Reader getReaderById(Integer id);
}
