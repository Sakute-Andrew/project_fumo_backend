package com.sakute.project_fumo_backend.domain.dto.user;

import com.sakute.project_fumo_backend.domain.enteties.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    AdminUserDto toAdminDto(User user);
}
