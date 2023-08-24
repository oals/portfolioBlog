package com.springstudy.blogportfolio.service;

import com.springstudy.blogportfolio.dto.RequestLoginUserDTO;
import com.springstudy.blogportfolio.dto.UserInfoDTO;
import com.springstudy.blogportfolio.entity.UserInfo;

import javax.servlet.http.Cookie;
import java.util.Map;

public interface User_Info_Service {


    public void User_Register(UserInfo userInfo);   //회원 가입

    public RequestLoginUserDTO User_Find(String userEmail);

    public boolean User_Check(String user_id);

    public String Get_UserNickame(String userEmail);

    public void Update_UserNickName(String userEmail,String userNickName); //유저 닉네임 변경


    public String find_userEmail(String name,String nickName,String phone);

    public String find_userPw(String name,String userEmail,String phone);



}
