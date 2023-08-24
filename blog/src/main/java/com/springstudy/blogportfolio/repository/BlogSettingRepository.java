package com.springstudy.blogportfolio.repository;

import com.springstudy.blogportfolio.dto.BlogSettingDTO;
import com.springstudy.blogportfolio.entity.BlogSetting;
import com.springstudy.blogportfolio.entity.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface BlogSettingRepository extends JpaRepository<BlogSetting,Long>, QuerydslPredicateExecutor<BlogSetting> {








}
