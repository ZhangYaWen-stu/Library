package com.library.mapper;

import com.library.pojo.Librarian;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LibrarianMapper {


    @Select("select * from librarian where Username = #{userName} and Password = #{password}")
    @Results(value = {
            @Result(property = "jobNumber", column = "GH", id = true),
            @Result(property = "name", column = "XM"),
            @Result(property = "password", column = "Password"),
            @Result(property = "userName", column = "Username")})
    Librarian getLibrarian(Librarian librarian);

    @Update("update librarian set Password = #{password} where GH = #{id}")
    void updateLibrarianPassword(@Param("password")String password,@Param("id") Integer id);
}
