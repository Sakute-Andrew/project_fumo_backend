// Створіть цей файл: src/main/java/com/sakute/project_fumo_backend/domain/enteties/dto/PaginatedResponseDto.java

package com.sakute.project_fumo_backend.domain.enteties.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaginatedResponseDto<T> {
    private List<T> posts;
    private int totalPages;
    private long total;
    private int currentPage;
    private int pageSize;


}