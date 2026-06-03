package com.sakute.project_fumo_backend.domain.dto.fundraising;

import com.sakute.project_fumo_backend.domain.enteties.fundraising.Fundraising;
import org.mapstruct.*;
import java.sql.Timestamp;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class FundraisingMapper { // Використовуємо abstract class, щоб додати логіку

    @Mapping(source = "category.id", target = "category")
    @Mapping(source = "description", target = "description", qualifiedByName = "truncateText")
    @Mapping(target = "progressPercentage", ignore = true)
    @Mapping(target = "daysLeft", ignore = true)
    public abstract FundraisingListDto toListDto(Fundraising entity);

    @Mapping(source = "owner.fullName", target = "userName") // Це залишаємо для красивого відображення
    @Mapping(source = "owner.username", target = "ownerUsername")// було userId.fullName
    @Mapping(source = "category.id", target = "category")   // додай це
    @Mapping(target = "progressPercentage", ignore = true)
    @Mapping(target = "daysLeft", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    public abstract FundraisingDto toDto(Fundraising entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "donations", ignore = true)  // було currentAmount — його більше нема
    @Mapping(target = "category", ignore = true)   // Long -> FundraisingCategory не мапиться автоматично
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEntityFromDto(FundraisingDto dto, @MappingTarget Fundraising entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "donations", ignore = true)
    @Mapping(target = "category", ignore = true)
    public abstract Fundraising toEntity(FundraisingDto dto);

    // 3. ✨ МАГІЯ: Спільна логіка обчислень для ОБОХ DTO ✨
    @AfterMapping
    protected void calculateMetrics(Fundraising entity, @MappingTarget Object dto) {

        // Рахуємо прогрес лише один раз
        int progress = 0;
        if (entity.getGoalAmount() != null && entity.getGoalAmount().doubleValue() > 0) {
            double calc = entity.getCurrentAmount().doubleValue() / entity.getGoalAmount().doubleValue() * 100;
            progress = (int) Math.min(Math.round(calc), 100);
        }

        // Рахуємо дні лише один раз
        int daysLeft = calculateDaysLeft(entity.getEndDate());

        // Розкидаємо результати залежно від того, яке DTO ми зараз будуємо
        if (dto instanceof FundraisingListDto listDto) {
            listDto.setProgressPercentage(progress);
            listDto.setDaysLeft(daysLeft);
        } else if (dto instanceof FundraisingDto fullDto) {
            fullDto.setProgressPercentage(progress);
            fullDto.setDaysLeft(daysLeft);
            // active потрібен тільки для повного DTO
            fullDto.setActive(entity.getEndDate() != null && entity.getEndDate().after(new Timestamp(System.currentTimeMillis())));
        }
    }

    // 4. Допоміжні методи (тепер вони живуть у мапері, а не засмічують сервіс)
    @Named("truncateText")
    protected String truncateDescription(String description) {
        if (description == null) return null;
        return description.length() > 150 ? description.substring(0, 150) + "..." : description;
    }

    protected int calculateDaysLeft(Timestamp endDate) {
        if (endDate == null) return 0;
        long diff = endDate.getTime() - System.currentTimeMillis();
        return diff > 0 ? (int) (diff / (1000 * 60 * 60 * 24)) : 0;
    }
}
