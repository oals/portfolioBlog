package com.springstudy.blogportfolio.entity;

import com.springstudy.blogportfolio.constant.Role;
import com.springstudy.blogportfolio.dto.UserInfoDTO;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;


@Log4j2
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "userinfo")
@Builder
@Entity
public class UserInfo {

    @Id
    @Column(nullable = false,length = 200)
    private String userEmail;

    @Column(nullable = false)
    private String userPw;

    @Column(nullable = false,length = 50)
    private String userName;

    @Column(nullable = true,length = 50)
    private String userNickName;


    @Column(nullable = false,length = 300)
    private String address;

    @Column(nullable = false,length = 50)
    private String phone;

    @Column(nullable = false)
    private String joinDate;


    @Column(nullable = true)
    private int followCount;

    @Column(nullable = true)
    private int followingCount;

    //role 추가
    @Enumerated(EnumType.STRING)
    private Role role;  //권한 설정



    public static UserInfo createMember(UserInfoDTO userInfoDTO, PasswordEncoder passwordEncoder){
        UserInfo userInfo = new UserInfo();
        userInfo.setUserEmail(userInfoDTO.getUserEmail());
        userInfo.setUserName(userInfoDTO.getUserName());
        userInfo.setUserNickName(userInfoDTO.getUserNickName());
        userInfo.setAddress(userInfoDTO.getAddress());
        userInfo.setPhone(userInfoDTO.getPhone());
        userInfo.setJoinDate(userInfoDTO.getJoinDate());
        userInfo.setFollowCount(userInfoDTO.getFollowCount());
        userInfo.setFollowingCount(userInfoDTO.getFollowingCount());
        userInfo.setRole(Role.USER);


        // 암호화
        String password = passwordEncoder.encode(userInfoDTO.getUserPw());
        userInfo.setUserPw(password);
//        member.setRole(Role.USER);
//        member.setRole(Role.ADMIN);

        return userInfo;
    }

    public void Update_NickName(String userNickName){
        this.userNickName = userNickName;
    }



}
