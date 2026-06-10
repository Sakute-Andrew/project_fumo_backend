package com.sakute.project_fumo_backend.domain.service;

import com.sakute.project_fumo_backend.domain.dto.fundraising.FundraisingCategoryDto;
import com.sakute.project_fumo_backend.domain.dto.fundraising.FundraisingDto;
import com.sakute.project_fumo_backend.domain.dto.fundraising.FundraisingListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface FundraisingService {
    Page<FundraisingListDto> getAllFundraising(Pageable pageable, Long category, String search);
    List<FundraisingCategoryDto> getCategories();
    Page<FundraisingListDto> getPopularFundraisings(Pageable pageable);
    FundraisingDto getFundraisingById(UUID id);
    Page<FundraisingListDto> getActiveFundraising(Pageable pageable);
    Page<FundraisingListDto> getEndingSoonFundraising(Pageable pageable);
    FundraisingDto createFundraising(FundraisingDto createDto);
    boolean isFundraisingOwner(UUID fundraisingId, String userEmail);
    FundraisingDto updateFundraising(UUID id, FundraisingDto updateDto);
    void deleteFundraising(UUID id);
}
