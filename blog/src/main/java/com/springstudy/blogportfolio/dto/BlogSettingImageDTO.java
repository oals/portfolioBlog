package com.springstudy.blogportfolio.dto;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BlogSettingImageDTO {    //수정 전 이미지 정보

    Long blogNo;
    String PastHeaderImagePath;  //수정 전 이미지 실제 경로
    String PastBodyImagePath;
    String PastProfileImagePath;

}
