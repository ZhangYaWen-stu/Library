package com.library.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private String msg;
    private Integer code;
    private Object data;
    public static Result success(){
        return new Result("Success", Code.SUCCESS, null);
    }
    public static Result fail(){
        return new Result("Fail", Code.FAIL, null);
    }
    public static Result success(Object data){
        return new Result("Success", Code.SUCCESS, data);
    }

    public static Result error(){
        return new Result("Error", Code.ERROR, null);
    }
    public static Result error(String msg){
        return new Result(msg, Code.ERROR, null);
    }

    public static Result fail(String msg){
        return new Result(msg, Code.FAIL, null);
    }
    public static Result fail(String msg, Integer code_){
        return new Result(msg, code_, null);
    }
}
