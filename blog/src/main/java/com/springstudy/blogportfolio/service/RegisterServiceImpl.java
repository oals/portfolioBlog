package com.springstudy.blogportfolio.service;

import com.springstudy.blogportfolio.repository.User_Info_Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class RegisterServiceImpl implements RegisterService{

    private final User_Info_Repository userInfoRepository;

    @Override
    public boolean userEmailCheck(String userEmail) {


       boolean chk =  userInfoRepository.findByuserEmail(userEmail) != null ? false : true;
       log.info("이메일 검사 결과 : " + chk);
        return chk;
    }

    @Override
    public boolean userNickNameCheck(String userNickName) {

        boolean chk = userInfoRepository.findByUserNickName(userNickName) != null ? false : true;
        log.info("닉네임 : " + userNickName);
        log.info("닉네임 검사 결과 : " + chk);

        return chk;
    }
}
