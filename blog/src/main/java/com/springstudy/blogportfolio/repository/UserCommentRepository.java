package com.springstudy.blogportfolio.repository;

import com.springstudy.blogportfolio.dto.UserCommentDTO;
import com.springstudy.blogportfolio.entity.UserBoard;
import com.springstudy.blogportfolio.entity.UserComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import javax.persistence.Column;
import java.util.List;
import java.util.Map;

public interface UserCommentRepository extends JpaRepository<UserComment,Long> , QuerydslPredicateExecutor<UserComment> {


    @Modifying(clearAutomatically = true)
    @Query("delete from UserComment u where u.userBoard.articleNo = :articleNo")
    void deleteByBoardComment(@Param("articleNo")Long articleNo);
    //사용안됨 예상


    @Query("select c from UserComment c where c.commentNo = :parentCommentNo")
    UserComment findByParent_CommentNo(@Param("parentCommentNo") Long parentCommentNo);




}
