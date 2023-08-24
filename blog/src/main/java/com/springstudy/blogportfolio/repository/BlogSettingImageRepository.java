package com.springstudy.blogportfolio.repository;

import com.springstudy.blogportfolio.entity.BlogSetting;
import com.springstudy.blogportfolio.entity.BlogSettingImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogSettingImageRepository extends JpaRepository<BlogSettingImage,Long> {

    BlogSettingImage findByBlogSetting_BlogNo(Long blogNo);


}
