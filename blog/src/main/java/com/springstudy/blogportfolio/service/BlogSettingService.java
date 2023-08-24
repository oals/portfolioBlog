package com.springstudy.blogportfolio.service;

import com.querydsl.core.Tuple;
import com.springstudy.blogportfolio.dto.*;
import com.springstudy.blogportfolio.entity.BlogSetting;
import com.springstudy.blogportfolio.entity.BlogSettingImage;
import com.springstudy.blogportfolio.entity.UserBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;


public interface BlogSettingService {

    public BlogSettingDTO create_blog(UserInfoDTO user_info_dto);   //기본 블로그 생성

    public void create_visit(BlogSettingDTO blogSettingDTO); // 방문기록 테이블 생성

    public void create_category(BlogSettingDTO blogSettingDTO);

    public void create_Image(BlogSettingDTO blogSettingDTO);


    // -------------------------------------------------------------------------


    public RequestBlogSettingDTO GetBlog_Setting(String userNickName);  //블로그 설정 정보 모두 가져오기
    
    public BlogSettingDTO GetBlog_SettingEntity(Long blogNo);  //블로그 엔티티 가져오기


    public  PageResponseDTO<RequestPopularBlogDTO> GetAll_Blog(PageRequestDTO pageRequestDTO);   //인기 블로그의 가져오기



    public CategoryDTO GetBlog_Category(Long blogNo);



    public BlogSettingImageDTO GetBlog_SettingImage(Long blogNo);





    //-------------------------------------------------------------------------

    public void update_BlogImage(Long blogNo, BlogSettingDTO blogSettingDTO);  //블로그 이미지 및 설정 정보 업데이트

    public void update_PastBlogImage(Long blogNo,BlogSettingImageDTO blogSettingImageDTO);

    public void update_Category(Long blogNo,String[] CategoryStr);


    public void delete_Category(Long blogNo);

    public void update_CategoryCount(Long blogNo,String category,boolean chk);

    //-----
    //테스트


}
