package com.springstudy.blogportfolio.entity;

import com.springstudy.blogportfolio.dto.BlogSettingDTO;
import com.springstudy.blogportfolio.dto.BlogSettingImageDTO;
import lombok.*;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogSettingImage {    //수정 전 이미지 정보


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long BlogImgNo;

    @OneToOne(fetch = FetchType.LAZY)    //확인 필요
    @JoinColumn(name="blogsetting_blog_no")
    BlogSetting blogSetting;

    String PastHeaderImagePath;  //수정 전 이미지 실제 경로
    String PastBodyImagePath;
    String PastProfileImagePath;

    public void update_Image(BlogSettingImageDTO blogSettingImageDTO){
        this.PastHeaderImagePath = blogSettingImageDTO.getPastHeaderImagePath();
        this.PastBodyImagePath = blogSettingImageDTO.getPastBodyImagePath();
        this.PastProfileImagePath = blogSettingImageDTO.getPastProfileImagePath();

    }



}
