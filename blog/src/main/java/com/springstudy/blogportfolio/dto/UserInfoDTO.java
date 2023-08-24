package com.springstudy.blogportfolio.dto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserInfoDTO {

    private String userEmail;
    private String userPw;
    private String userName;
    private String userNickName;
    private String address;
    private String phone;
    private String JoinDate;
    private int followCount;
    private int followingCount;
    private String uuid;





}
