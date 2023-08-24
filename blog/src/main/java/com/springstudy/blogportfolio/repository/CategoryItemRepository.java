package com.springstudy.blogportfolio.repository;

import com.springstudy.blogportfolio.entity.CategoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CategoryItemRepository extends JpaRepository<CategoryItem,Long> {




}
