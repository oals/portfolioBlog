package com.springstudy.blogportfolio.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springstudy.blogportfolio.entity.BlogSetting;
import com.springstudy.blogportfolio.entity.BoardImage;
import com.springstudy.blogportfolio.entity.UserBoard;
import com.springstudy.blogportfolio.entity.UserComment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class TopicBoardDTO {


    private String userEmail;
    private String userNickName;
    private String profileImagePath;

    private String imagePath;
    private String title;
    private String content;
    private LocalDate writeDate;


}
