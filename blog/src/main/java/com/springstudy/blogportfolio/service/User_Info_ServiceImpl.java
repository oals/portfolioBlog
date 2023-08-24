package com.springstudy.blogportfolio.service;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springstudy.blogportfolio.dto.RequestLoginUserDTO;
import com.springstudy.blogportfolio.dto.UserInfoDTO;
import com.springstudy.blogportfolio.entity.QBlogSetting;
import com.springstudy.blogportfolio.entity.QUserInfo;
import com.springstudy.blogportfolio.entity.QVisit;
import com.springstudy.blogportfolio.entity.UserInfo;
import com.springstudy.blogportfolio.repository.User_Info_Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Cookie;
import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Log4j2

public class User_Info_ServiceImpl implements User_Info_Service {

    private final User_Info_Repository user_info_repository;
    private final ModelMapper modelMapper;

    @PersistenceContext
    EntityManager em;




    @Override
    public void User_Register(UserInfo userInfo) {        //회원가입

        validateDuplicateMember(userInfo); //가입된 회원인지 검사

        log.info("회원가입 테스트 " + userInfo);

        user_info_repository.save(userInfo); //데이터 베이스 저장
    }



    private void validateDuplicateMember(UserInfo userInfo){
        Optional<UserInfo> result = user_info_repository.findById(userInfo.getUserEmail());

        // 이미 가입된 회원인 경우 예외 발생
        if (result == null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }

    }


    @Override
    public RequestLoginUserDTO User_Find(String userEmail) {  //유저 정보 찾기



        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBlogSetting qBlogSetting = QBlogSetting.blogSetting;
        QUserInfo qUserInfo = QUserInfo.userInfo;
        QVisit qVisit = QVisit.visit;

        JPAQuery<RequestLoginUserDTO> query =  queryFactory.select(Projections.bean(RequestLoginUserDTO.class,
                        qUserInfo,
                        qBlogSetting.profileImagePath,
                        qVisit.today,
                        qVisit.total
                ))
                .from(qUserInfo)
                .join(qBlogSetting)
                .on(qUserInfo.userEmail.eq(qBlogSetting.userInfo.userEmail))
                .join(qVisit)
                .on(qBlogSetting.blogNo.eq(qVisit.blogsetting.blogNo))
                .where(qBlogSetting.userInfo.userEmail.eq(userEmail));




        RequestLoginUserDTO requestLoginUserDTO = query.fetchOne();
        
        log.info(requestLoginUserDTO.getProfileImagePath());




    return requestLoginUserDTO;
    }






    @Override
    public boolean User_Check(String user_id) {

        boolean chk = false;

        if(user_info_repository.findById(user_id).isEmpty()){
            chk = true;  //가입되지 않은 회원일 때
        }

        return chk;
    }

    @Override
    public String Get_UserNickame(String userEmail) {

        String userNickName = user_info_repository.findById(userEmail).orElseThrow().getUserNickName();
        log.info("유저 닉네임 : " + userNickName);

        return userNickName;
    }


    @Override
    public void Update_UserNickName(String userEmail,String userNickName) {   //유저 닉네임 변경
        UserInfo userInfo = user_info_repository.findById(userEmail).orElseThrow();

        userInfo.Update_NickName(userNickName);
        user_info_repository.save(userInfo);

    }

    @Override
    public String find_userEmail(String name, String nickName, String phone) {



        String userEmail = user_info_repository.findUserEmail(name,nickName,phone);


        return userEmail;
    }

    @Override
    public String find_userPw(String name, String userEmail, String phone) {


        String userPw = user_info_repository.findUserPw(name,userEmail,phone);
        log.info("비번 : " + userPw);

        return userPw;

    }


}
