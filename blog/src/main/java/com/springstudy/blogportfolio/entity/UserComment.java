package com.springstudy.blogportfolio.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Log4j2
public class UserComment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentNo;   //오타 있음

    //@JsonIgnore   //객체를 JSON 포멧으로 변환할 때, 변환하지 않고 무시해야할 멤버 변수

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userboard_articleNo")
    private UserBoard userBoard;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="blogsetting_blogNo")
    private BlogSetting blogSetting;  //userinfo 엮임
    

    @Column(nullable = false,name="user_nick_name")
    private String userNickName;


    @Column(nullable = false,name="comment")
    private String comment;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="UserComment_commentNo")
    private UserComment parentComment;              //null일시 원본 댓글


    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="UserComment_commentNo")
    private List<UserComment> childComment = new ArrayList<UserComment>();



    @Column(nullable = false)
    private LocalDate writeDate;



    public void addChildComment(UserComment userComment){

        this.childComment.add(userComment);

    }



}
