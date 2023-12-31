package com.springstudy.blogportfolio.dto;

import com.springstudy.blogportfolio.entity.BlogSetting;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestBlogSettingDTO {


    private BlogSetting blogSetting;
    private String userNickName;
    private String userEmail;
    private String JoinDate;
    private int today;
    private int total;


}
