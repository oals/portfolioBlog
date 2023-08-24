package com.springstudy.blogportfolio.repository;

import com.springstudy.blogportfolio.dto.VisitDTO;
import com.springstudy.blogportfolio.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface VisitRepository extends JpaRepository<Visit,Long>,QuerydslPredicateExecutor<Visit> {

    Visit findByBlogsetting_BlogNo(Long blogNo); // 해당 블로그의 방문 횟수 가져오기



}
