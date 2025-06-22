package com.sakute.project_fumo_backend.domain.enteties.dto.mapper;

import com.sakute.project_fumo_backend.domain.enteties.dto.IntellectualPropertyDto;
import com.sakute.project_fumo_backend.domain.enteties.intprop.IntellectualProperty;
import com.sakute.project_fumo_backend.domain.enteties.intprop.IntellectualPropertyCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IntellectualPropertyMapper {

    @Mapping(target = "userFullname", source = "owner.fullName")
    @Mapping(target = "intellectualPropertyCategory", source = "intellectualPropertyCategory")
    IntellectualPropertyDto toDto(IntellectualProperty entity);

    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "intellectualPropertyCategory", source = "intellectualPropertyCategory")
    IntellectualProperty toEntity(IntellectualPropertyDto dto);

    // Custom mapping methods
    default String map(IntellectualPropertyCategory category) {
        return category != null ? category.getCategoryName() : null; // або getTitle() — залежно від поля
    }

    default IntellectualPropertyCategory map(String categoryName) {
        if (categoryName == null) return null;

        IntellectualPropertyCategory category = new IntellectualPropertyCategory();
        category.setCategoryName(categoryName); // або setTitle()
        return category;
    }
}
