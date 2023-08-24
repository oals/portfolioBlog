package com.springstudy.blogportfolio.repository;

import com.springstudy.blogportfolio.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface User_Info_Repository extends JpaRepository<UserInfo,String>, QuerydslPredicateExecutor<UserInfo> {


    UserInfo findByUserNickName(String userNickName);

    UserInfo findByuserEmail(String email);

    @Query("select u.userEmail from UserInfo u where u.userName = :name " +
            "and u.userNickName = :nickName " +
            "and u.phone = :phone")
    String findUserEmail(@Param("name") String name, @Param("nickName") String nickName,@Param("phone") String phone);


    @Query("select u.userPw from UserInfo u where u.userName = :name " +
            "and u.userEmail = :userEmail " +
            "and u.phone = :phone")
    String findUserPw(@Param("name") String name, @Param("userEmail") String userEmail,@Param("phone") String phone);




}
