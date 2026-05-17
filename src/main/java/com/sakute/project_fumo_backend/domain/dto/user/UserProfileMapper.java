package com.sakute.project_fumo_backend.domain.dto.user;

import com.sakute.project_fumo_backend.domain.enteties.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileMapper {

    @Mapping(source = "userId", target = "id")
    @Mapping(source = "username", target = "userName")
    // MapStruct сам перевірить, чи userProfile не дорівнює null перед тим, як брати поля
    @Mapping(source = "userProfile.website", target = "website")
    @Mapping(source = "userProfile.location", target = "location")
    // Вказуємо, що для цього поля треба використати наш кастомний метод
    UserProfileDto toDto(User user);

    // Допоміжний метод для перетворення рядка зі скілами у список
    @Named("stringToList")
    default List<String> stringToList(String areas) {
        if (areas == null || areas.trim().isEmpty()) {
            return Collections.emptyList();
        }
        // split("\\s*,\\s*") автоматично прибере зайві пробіли після ком,
        // якщо юзер ввів "Java, PHP, React" замість "Java,PHP,React"
        return Arrays.asList(areas.split("\\s*,\\s*"));
    }
}