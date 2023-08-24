package com.springstudy.blogportfolio.entity;


import lombok.*;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="category")
@Log4j2
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="blogsetting_blog_no")
    private BlogSetting blogSetting;


    @OneToMany(mappedBy = "category" ,cascade = CascadeType.PERSIST)
    private List<CategoryItem> categoryItem = new ArrayList<CategoryItem>();



    public void addCategory(List<CategoryItem> categoryItem){

        for(int i = 0; i< categoryItem.size(); i++) {
            log.info("카테고리 아이템 add 접근");
            this.categoryItem.add(categoryItem.get(i));
        }
    }






}
