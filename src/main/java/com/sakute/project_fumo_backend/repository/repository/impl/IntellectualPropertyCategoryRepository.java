package com.sakute.project_fumo_backend.repository.repository.impl;

import com.sakute.project_fumo_backend.domain.enteties.IntellectualPropertyCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IntellectualPropertyCategoryRepository extends JpaRepository<IntellectualPropertyCategory, Long> {
    Optional<IntellectualPropertyCategory> findByCategoryId(Long categoryId);
    Optional<IntellectualPropertyCategory> findByCategoryName(String categoryName);
}
