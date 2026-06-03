package com.sakute.project_fumo_backend.domain.dto.user_profile;

import com.sakute.project_fumo_backend.domain.enteties.fundraising.Fundraising;
import com.sakute.project_fumo_backend.domain.enteties.intprop.IntellectualProperty;
import com.sakute.project_fumo_backend.domain.enteties.post.UserPost;
import com.sakute.project_fumo_backend.domain.enteties.user.Permission;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = Permission.class)
public interface CreatorProfileMapper {

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "userProfile.bio", target = "bio")
    @Mapping(source = "userProfile.areasOfExpertise", target = "areasOfExpertise")
    @Mapping(source = "userProfile.location", target = "location")
    @Mapping(source = "userProfile.website", target = "website")
    @Mapping(target = "canFundraise", expression = "java(user.getPermissions().contains(Permission.CAN_FUNDRAISE))")
    @Mapping(target = "canSellIp", expression = "java(user.getPermissions().contains(Permission.CAN_SELL_IP))")
    UserSummaryDTO toUserSummary(User user);

    default PostSummaryDTO toPostSummary(UserPost post) {
        return new PostSummaryDTO(
                post.getUserPostId(),
                post.getPostHeader(),
                post.getPostDescription(),
                post.getPhoto(),
                post.getCreatedAt(),
                post.getComments().size()
        );
    }

    default FundraisingSummaryDTO toFundraisingSummary(Fundraising f) {
        return new FundraisingSummaryDTO(
                f.getId(),
                f.getTitle(),
                f.getDescription(),
                f.getGoalAmount(),
                f.getCurrentAmount(),
                f.getEndDate(),
                f.getStatus()
        );
    }

    default IpSummaryDTO toIpSummary(IntellectualProperty ip) {
        return new IpSummaryDTO(
                ip.getIpId(),
                ip.getName(),
                ip.getDescription(),
                ip.getTypeIp(),
                ip.getStatus()
        );
    }
}