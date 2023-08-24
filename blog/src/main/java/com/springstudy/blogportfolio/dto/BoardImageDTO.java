package com.springstudy.blogportfolio.dto;

import com.springstudy.blogportfolio.entity.UserBoard;
import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BoardImageDTO {

    private Long imageNo;

    private Long articleNo;

    private String imagePath;



}
