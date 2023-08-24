package com.springstudy.blogportfolio.dto;

import com.springstudy.blogportfolio.entity.UserInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RequestLoginUserDTO {

    private UserInfo userInfo;
    private int today;
    private int total;
    private String profileImagePath;

}
