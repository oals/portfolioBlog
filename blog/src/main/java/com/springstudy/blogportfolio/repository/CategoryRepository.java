package com.springstudy.blogportfolio.repository;

import com.springstudy.blogportfolio.dto.CategoryDTO;
import com.springstudy.blogportfolio.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface CategoryRepository extends JpaRepository<Category,Long> {



}
