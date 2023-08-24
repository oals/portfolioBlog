package com.springstudy.blogportfolio.security;

import com.springstudy.blogportfolio.entity.UserInfo;
import com.springstudy.blogportfolio.repository.User_Info_Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
//@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


    private final User_Info_Repository userInfoRepository;




    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        UserInfo userInfo = userInfoRepository.findByuserEmail(userEmail);
        log.info("-0--"+ userInfo);

        if(userInfo == null){  //회원 정보가 없다
            throw new UsernameNotFoundException(userEmail);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        //db로 회원 정보
        return new CustomUserMember(userInfo,authorities);


/*                User.builder()
                .username(userInfo.getUserEmail())  //unique 설정한 칼럼만 가능
                .password(userInfo.getUserPw())
                .roles(userInfo.getRole().toString())
                .build();*/
    }








}
