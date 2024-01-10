package com.library.pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Librarian {
    Integer jobNumber;
    String name;
    String userName;
    @JsonIgnore
    String password;
}
