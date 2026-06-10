package com.sakute.project_fumo_backend.domain.service;

import com.sakute.project_fumo_backend.domain.dto.int_prop.IntellectualPropertyCategoryDto;
import com.sakute.project_fumo_backend.domain.dto.int_prop.IntellectualPropertyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IntellectualPropertyService {
    Page<IntellectualPropertyDto> findAll(Long categoryId, String status, String name, Pageable pageable);
    Page<IntellectualPropertyDto> findAll(Pageable pageable);
    IntellectualPropertyDto findById(UUID id);
    List<IntellectualPropertyDto> findByName(String name);
    List<IntellectualPropertyDto> findByCategory(Long categoryId);
    List<IntellectualPropertyCategoryDto> getAllCategories();
    List<IntellectualPropertyDto> findByCurrentUser();
    IntellectualPropertyDto create(IntellectualPropertyDto dto);
    IntellectualPropertyCategoryDto createCategory(IntellectualPropertyCategoryDto dto);
    void deleteCategory(long id);
    IntellectualPropertyDto update(UUID id, IntellectualPropertyDto dto);
    void delete(UUID id);
    IntellectualPropertyDto changeStatus(UUID id, String statusStr);
    boolean isOwner(UUID id);
}
