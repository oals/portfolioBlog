package com.springstudy.blogportfolio.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springstudy.blogportfolio.entity.BlogSetting;
import com.springstudy.blogportfolio.entity.UserBoard;
import com.springstudy.blogportfolio.entity.UserComment;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class UserCommentDTO {

    private Long commentNo;

    @JsonIgnore
    private UserBoard userBoard;

    @JsonIgnore
    private BlogSetting blogSetting;

    private String userNickName;

    private String comment;

    private UserComment parentComment;

    private List<UserComment> childComment = new ArrayList<UserComment>();      //null인 칼럼만 조회시 여기서 자식 댓글을 꺼내올수잇을거같은데

    private LocalDate writeDate;







}
