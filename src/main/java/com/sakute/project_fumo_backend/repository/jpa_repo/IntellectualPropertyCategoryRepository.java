package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.IntellectualPropertyCategory;
import com.sakute.project_fumo_backend.repository.RepositoryFactory;

import java.util.Optional;

public interface IntellectualPropertyCategoryRepository extends RepositoryFactory<IntellectualPropertyCategory, Long> {
    Optional<IntellectualPropertyCategory> findByCategoryId(Long categoryId);
    Optional<IntellectualPropertyCategory> findByCategoryName(String categoryName);
}
