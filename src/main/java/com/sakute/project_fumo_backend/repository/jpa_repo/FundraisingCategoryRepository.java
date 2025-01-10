package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.FundraisingCategory;
import com.sakute.project_fumo_backend.repository.RepositoryFactory;

public interface FundraisingCategoryRepository extends RepositoryFactory<FundraisingCategory, Long> {
    FundraisingCategory findFundraisingCategoriesById(Long categoryId);
    FundraisingCategory findByCategoryName(String categoryName);
}
