package com.springstudy.blogportfolio.restcontroller;

import com.springstudy.blogportfolio.service.RegisterService;
import com.springstudy.blogportfolio.service.RegisterServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterRestController {

    private final RegisterService registerService;

    @GetMapping(value = "userEmailChk")
    public boolean userEmailChk(String userEmail){

        boolean chk = false;


        chk = registerService.userEmailCheck(userEmail);



        return chk;

    }


    @GetMapping(value = "userNickNameChk")
    public boolean userNickNameChk(String userNickName){

        boolean chk = false;


        chk = registerService.userNickNameCheck(userNickName);



        return chk;

    }




}
