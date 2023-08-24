package com.springstudy.blogportfolio.dto;

import com.springstudy.blogportfolio.entity.UserInfo;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BlogSettingDTO {


    private Long blogNo;
    private UserInfo userInfo;
    private String blogTopic;
    private String headerImagePath;
    private String bodyImagePath;
    private String profileInfo;
    private String profileImagepath;
    private boolean privateChk;
    private boolean writePermission;










}
