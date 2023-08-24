package com.springstudy.blogportfolio.restcontroller;

import com.springstudy.blogportfolio.service.User_Info_Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class LoginRestController {

    private final User_Info_Service user_info_service;

    @GetMapping(value="findEmail")
    public String findEmail(String name, String nickName,String phone){

        String userEmail = user_info_service.find_userEmail(name,nickName,phone);


        return userEmail;
    }


    @GetMapping(value="findPw")
    public String findPw(String name, String userEmail,String phone){

        log.info(name);
        log.info(userEmail);
        log.info(phone);

        String userPw = user_info_service.find_userPw(name,userEmail,phone);



        return userPw;
    }
















}
