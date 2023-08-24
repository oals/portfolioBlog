package com.springstudy.blogportfolio.dto;

import com.springstudy.blogportfolio.entity.BlogSetting;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "blogsetting")
public class VisitDTO {
    private Long visitNo;
    private BlogSetting blogsetting;
    private int today;
    private int total;



}
