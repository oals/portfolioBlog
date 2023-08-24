package com.springstudy.blogportfolio.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springstudy.blogportfolio.entity.BlogSetting;
import com.springstudy.blogportfolio.entity.BoardImage;
import com.springstudy.blogportfolio.entity.UserComment;
import com.springstudy.blogportfolio.entity.UserInfo;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(exclude = "blogSetting")
public class UserBoardDTO {

    private Long articleNo;

    @JsonIgnore
    private BlogSetting blogSetting;

    @JsonIgnore
    private List<UserComment> userComment = new ArrayList<UserComment>();

    private BoardImage Thumbnail;

    private String category;
    private String title;
    private String content;

    private String view;
    private String like;
    private LocalDate writeDate;

}
