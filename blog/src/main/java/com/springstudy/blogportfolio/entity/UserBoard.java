package com.springstudy.blogportfolio.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.*;
import org.modelmapper.internal.Errors;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="userboard")
public class UserBoard {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long articleNo;

    @ManyToOne(fetch = FetchType.LAZY)    //확인 필요
    @JoinColumn(name="blogsetting_blog_no")
    private BlogSetting blogSetting;


    @OneToMany(mappedBy = "userBoard",cascade = CascadeType.ALL)
    private List<UserComment> userComment = new ArrayList<UserComment>();


    @OneToOne(cascade = CascadeType.ALL)
    private BoardImage Thumbnail;



    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false,length = 5000)
    private String content;

    @Min(value=0)
    @Column(nullable = false)
    private int view;

    @Min(value=0)
    @Column(nullable = false,name = "board_Like")
    private int like;

    @Column(nullable = false)
    private LocalDate writeDate;


    public void updateLike(){
        this.like += 1;

    }




}
