package com.library.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reader {
    String name;
    String telephoneNumber;
    String email;
    Integer borrowNum;
    String userName;
    @JsonIgnore
    String password;
    Integer id;
    String avatar;
}
