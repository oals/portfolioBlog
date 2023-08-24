package com.springstudy.blogportfolio.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Getter
@Setter
@Table(name="categoryItem")
public class CategoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="category_name")
    private String CategoryName;

    @Min(value = 0)
    @Column(name="category_count")
    private int categoryCount;

    @ManyToOne
    private Category category;





}
