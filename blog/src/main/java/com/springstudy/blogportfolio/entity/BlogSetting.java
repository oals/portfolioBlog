package com.springstudy.blogportfolio.entity;

import com.springstudy.blogportfolio.dto.BlogSettingDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Map;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="blogsetting")
@Builder
public class BlogSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blogNo;


    @OneToOne(fetch = FetchType.LAZY)    //확인 필요
    @JoinColumn(name = "userinfo_userEmail")
    private UserInfo userInfo;

    @Column(nullable = true)
    private String blogTopic;

    @Column(nullable = true)
    private String headerImagePath;

    @Column(nullable = true)
    private String bodyImagePath;

    @Column(nullable = true)
    private String profileInfo;

    @Column(nullable = true)
    private String profileImagePath;

    @Column(nullable = true)
    private boolean privateChk;

    @Column(nullable = true)
    private boolean writePermission;


    public void update_profile(BlogSettingDTO blogSettingDTO){


        this.profileInfo = blogSettingDTO.getProfileInfo();
        this.blogTopic = blogSettingDTO.getBlogTopic();
        this.privateChk = blogSettingDTO.isPrivateChk();
        this.headerImagePath = blogSettingDTO.getHeaderImagePath();
        this.bodyImagePath =  blogSettingDTO.getBodyImagePath();
        this.profileImagePath = blogSettingDTO.getProfileImagepath();
    }






}
