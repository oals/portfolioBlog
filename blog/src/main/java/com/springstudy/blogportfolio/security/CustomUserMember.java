package com.springstudy.blogportfolio.security;

import com.springstudy.blogportfolio.entity.UserInfo;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
@Log4j2
public class CustomUserMember extends User {
    private final String userEmail;
    private final String userPw;
    private final String userNickName;

    // member.getEmail()이 User객체에서 Id역할: User의 Id는 유일성을 보장하는 필드이여함.
    public CustomUserMember(UserInfo userInfo, List<GrantedAuthority> authorities) {
        super(userInfo.getUserEmail(), userInfo.getUserPw(), authorities);
        log.info("접근 체크");
        //User객체가 가지는 username과 password 이외에 id,getAddress,password 추가.
        this.userEmail = userInfo.getUserEmail();
        this.userPw = userInfo.getUserPw();
        this.userNickName = userInfo.getUserNickName();

    }
}
