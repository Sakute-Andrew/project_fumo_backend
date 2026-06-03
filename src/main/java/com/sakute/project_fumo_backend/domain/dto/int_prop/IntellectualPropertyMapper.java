package com.sakute.project_fumo_backend.domain.dto.int_prop;

import com.sakute.project_fumo_backend.domain.enteties.intprop.IntellectualProperty;
import com.sakute.project_fumo_backend.domain.enteties.intprop.IntellectualPropertyCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IntellectualPropertyMapper {

    // Мапінг Entity -> DTO
    @Mapping(target = "ipId", source = "ipId")
    @Mapping(target = "userFullname", source = "owner.fullName")
    @Mapping(target = "ownerId", source = "owner.userId")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "categoryId", source = "intellectualPropertyCategory.categoryId")
    @Mapping(target = "categoryName", source = "intellectualPropertyCategory.categoryName")
    IntellectualPropertyDto toDto(IntellectualProperty entity);

    // Мапінг DTO -> Entity
    @Mapping(target = "ipId", source = "ipId")
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "intellectualPropertyCategory", source = "categoryId")
    IntellectualProperty toEntity(IntellectualPropertyDto dto);

    /**
     * Кастомний метод: перетворює Long categoryId (з DTO)
     * у сутність IntellectualPropertyCategory (для Entity).
     */
    default IntellectualPropertyCategory mapCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        IntellectualPropertyCategory category = new IntellectualPropertyCategory();
        category.setCategoryId(categoryId);
        return category;
    }
}