package com.sakute.project_fumo_backend.domain.dto.fundraising;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundraisingCategoryDto {
    private Long id;
    private String categoryName;
}
