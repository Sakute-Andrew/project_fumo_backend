package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.fundraising.FundraisingCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundraisingCategoryRepository extends JpaRepository<FundraisingCategory, Long>{
    FundraisingCategory findFundraisingCategoriesById(Long categoryId);
    FundraisingCategory findByCategoryName(String categoryName);
}
