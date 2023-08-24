package com.springstudy.blogportfolio.repository;

import com.springstudy.blogportfolio.dto.UserBoardDTO;
import com.springstudy.blogportfolio.entity.UserBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface UserBoardRepository extends JpaRepository<UserBoard,Long> , QuerydslPredicateExecutor<UserBoard> {


    @Query("select new map( b.articleNo as articleNo, b.category as category, b.content as content, b.like as boardLike," +
            " b.title as title, b.view as view, b.writeDate as writeDate) from  UserBoard b where b.blogSetting.blogNo = :blogNo ORDER BY articleNo desc")
    Page<Map<String,Object>>findByBlogBoard(@Param("blogNo")Long blogNo, Pageable pageable);



    @Query("select max(u.articleNo) from UserBoard u")
    String findByArticleCount();  //해당 블로그의 글 개수 가져오기 (max 혹은 count)



    @Query("SELECT b.like from UserBoard b where b.articleNo = :articleNo")
    int findByBoardLike(@Param("articleNo") Long articleNo);
    //블로그 정보 추가


    @Query("SELECT b from UserBoard b where b.blogSetting.blogNo = :blogNo ORDER BY b.like DESC")
    List<UserBoard> findByPopularBoard(Long blogNo,PageRequest pageRequest);






}
