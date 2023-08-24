package com.springstudy.blogportfolio.dto;

import com.springstudy.blogportfolio.entity.BlogSetting;
import com.springstudy.blogportfolio.entity.CategoryItem;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CategoryDTO {

    private Long categoryNo;

    private BlogSetting blogSetting;


    private List<CategoryItem> categoryItem = new ArrayList<CategoryItem>();


}