package com.springstudy.shop.repository;


import com.springstudy.blogportfolio.dto.UserInfoDTO;
import com.springstudy.blogportfolio.entity.UserComment;
import com.springstudy.blogportfolio.entity.UserInfo;
import com.springstudy.blogportfolio.repository.UserCommentRepository;
import com.springstudy.blogportfolio.repository.User_Info_Repository;
import com.springstudy.blogportfolio.service.User_Info_Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@SpringBootTest
@SpringBootApplication
@Log4j2
@TestPropertySource(locations = "classpath:application.properties")
public class TestRepository {


    @Autowired
    private UserCommentRepository userCommentRepository;

    @Test
    public void userTest(){


        log.info("댓글 lazy 테스트----");
        UserComment userComment = userCommentRepository.findById(2L).orElseThrow();
        log.info(userComment.getBlogSetting());

        }




}
