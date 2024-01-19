package com.library.mapper;

import com.library.pojo.Librarian;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LibrarianMapper {


    @Select("select * from librarian where username = #{userName} and password = #{password}")
    @Results(value = {
            @Result(property = "jobNumber", column = "librarian_job_number", id = true),
            @Result(property = "name", column = "name"),
            @Result(property = "password", column = "password"),
            @Result(property = "userName", column = "username")})
    Librarian getLibrarian(Librarian librarian);

    @Update("update librarian set password = #{password} where librarian_job_number = #{id}")
    void updateLibrarianPassword(@Param("password")String password,@Param("id") Integer id);
}
