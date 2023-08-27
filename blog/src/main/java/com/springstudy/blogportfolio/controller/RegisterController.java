package com.springstudy.blogportfolio.controller;


import com.springstudy.blogportfolio.dto.BlogSettingDTO;
import com.springstudy.blogportfolio.dto.UserInfoDTO;
import com.springstudy.blogportfolio.entity.UserInfo;
import com.springstudy.blogportfolio.service.BlogSettingService;
import com.springstudy.blogportfolio.service.CreateDirectoryService;
import com.springstudy.blogportfolio.service.User_Info_Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Log4j2
@RequiredArgsConstructor
public class RegisterController {

    private final ModelMapper modelMapper;
    private final BlogSettingService blogSettingService;
    private final User_Info_Service user_info_service;
    private final PasswordEncoder passwordEncoder;

    private final CreateDirectoryService createDirectoryService;

    @GetMapping(value="/registerPage")
    public String registerPage(){

        log.info("register go ...");

        return "register/registerPage";
    }






    @PostMapping(value="/register")
    public ModelAndView register(UserInfoDTO user_info_dto){

        UserInfo userInfo = UserInfo.createMember(user_info_dto,passwordEncoder);

        //회원 정보 저장
        user_info_service.User_Register(userInfo);


        //기본 블로그 생성
        BlogSettingDTO blogSettingDTO = blogSettingService.create_blog(user_info_dto);


        //기본 조회수 테이블 생성
        blogSettingService.create_visit(blogSettingDTO);


        //기본 카테고리 테이블 생성
        blogSettingService.create_category(blogSettingDTO);


        //프로필 이미지 등 저장할 회원 폴더 생성
        createDirectoryService.createBlogDirectory(blogSettingDTO.getBlogNo());



        //프로필 이미지 등 업데이트 전 이미지 테이블 생성
        blogSettingService.create_Image(blogSettingDTO);


        //컨트롤러 -> 컨트롤러 이동 코드
        ModelAndView MAV = new ModelAndView();
        MAV.setViewName("redirect:/");

        return MAV;

    }




}
