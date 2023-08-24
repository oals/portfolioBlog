package com.springstudy.blogportfolio.controller;


import com.querydsl.core.Tuple;
import com.springstudy.blogportfolio.constant.Role;
import com.springstudy.blogportfolio.dto.*;
import com.springstudy.blogportfolio.entity.UserBoard;
import com.springstudy.blogportfolio.entity.UserComment;
import com.springstudy.blogportfolio.entity.UserInfo;
import com.springstudy.blogportfolio.global.FindCookie;
import com.springstudy.blogportfolio.repository.UserCommentRepository;
import com.springstudy.blogportfolio.repository.User_Info_Repository;
import com.springstudy.blogportfolio.repository.VisitRepository;
import com.springstudy.blogportfolio.service.BlogSettingService;
import com.springstudy.blogportfolio.service.UserBoardService;
import com.springstudy.blogportfolio.service.User_Info_Service;
import com.springstudy.blogportfolio.service.VisitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Null;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@Log4j2
@RequiredArgsConstructor
public class MainPageController {


    private final User_Info_Repository user_info_repository;
    private final User_Info_Service userInfoService;
    private final VisitService visitService;
    private final BlogSettingService blogSettingService;
    private final UserBoardService userBoardService;
    private final PasswordEncoder passwordEncoder;



    @GetMapping(value="/")
    public String mainpage(Model model,PageRequestDTO pageRequestDTO) {


        log.info("main page Go...");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        if (!principal.equals("anonymousUser")) {  //로그인 상태인 경우
            UserDetails userDetails = (UserDetails) principal;

            //로그인한 유저 정보 ex) 프로필 ,닉네임 가져오기 + 방문자 수
            RequestLoginUserDTO requestLoginUserDTO = userInfoService.User_Find(((UserDetails) principal).getUsername());
            model.addAttribute("userMap",requestLoginUserDTO);

        }

        //total 로 정렬한 인기 블로그순 가져오기
        PageResponseDTO<RequestPopularBlogDTO> BlogListDTO = blogSettingService.GetAll_Blog(pageRequestDTO);
        log.info("페이징테스트: "+BlogListDTO);



        //최신글 카테고리별 , 전체 가져오기
        PageResponseDTO<TopicBoardDTO> BoardListDTO = userBoardService.GetTopic_Board(pageRequestDTO, "all");
        //전체글 순서대로 출력


        //버튼 누른 값 전달 받기 -> rest에서만



        model.addAttribute("responseDTO",BlogListDTO);
        model.addAttribute("responseDTO2",BoardListDTO);

        return "index";
    }







}




