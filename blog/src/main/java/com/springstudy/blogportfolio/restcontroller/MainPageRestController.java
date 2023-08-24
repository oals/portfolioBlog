package com.springstudy.blogportfolio.restcontroller;

import com.querydsl.core.Tuple;
import com.springstudy.blogportfolio.dto.*;
import com.springstudy.blogportfolio.repository.User_Info_Repository;
import com.springstudy.blogportfolio.service.BlogSettingService;
import com.springstudy.blogportfolio.service.UserBoardService;
import com.springstudy.blogportfolio.service.User_Info_Service;
import com.springstudy.blogportfolio.service.VisitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Log4j2
@RequiredArgsConstructor
public class MainPageRestController {

    private final User_Info_Service userInfoService;
    private final BlogSettingService blogSettingService;
    private final UserBoardService userBoardService;

    @GetMapping(value="/popularBlog")
    public Object popularBlog(PageRequestDTO pageRequestDTO){


        log.info("main page 페이징 접근 Go...");

        PageResponseDTO<RequestPopularBlogDTO> BlogListDTO = blogSettingService.GetAll_Blog(pageRequestDTO);
        log.info("페이징테스트: "+BlogListDTO);


        return BlogListDTO;
    }


    @GetMapping(value="/topicBoard")
    public Object topicBoard(PageRequestDTO pageRequestDTO,String topic){  //해당 버튼 값 인자 추가


        PageResponseDTO<TopicBoardDTO> userBoardDTO = userBoardService.GetTopic_Board(pageRequestDTO,topic);



        return userBoardDTO;
    }









}
