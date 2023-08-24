package com.springstudy.blogportfolio.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestPopularBlogDTO {


    private String userEmail;
    private String userNickName;
    private String profileImagePath;
    private int total;



}
