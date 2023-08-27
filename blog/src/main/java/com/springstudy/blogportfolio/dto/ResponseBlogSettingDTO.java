package com.springstudy.blogportfolio.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.repository.query.Param;


@Getter
@Setter
public class ResponseBlogSettingDTO {

    Long blogNo;
    String userNickName;
    String profileInfo;
    boolean privateChk;
    String blogTopic;
    String[] parentCategory;


}
