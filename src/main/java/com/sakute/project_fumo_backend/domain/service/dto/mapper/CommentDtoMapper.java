package com.sakute.project_fumo_backend.domain.service.dto.mapper;

import com.sakute.project_fumo_backend.domain.enteties.Comment;
import com.sakute.project_fumo_backend.domain.service.dto.CommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentDtoMapper {

    Comment mapToDto(CommentDto commentDto);

    CommentDto mapToDto(Comment comment);

}
