package com.sakute.project_fumo_backend.domain.dto.user_profile;

import com.sakute.project_fumo_backend.domain.enteties.fundraising.Fundraising;
import com.sakute.project_fumo_backend.domain.enteties.intprop.IntellectualProperty;
import com.sakute.project_fumo_backend.domain.enteties.post.UserPost;
import com.sakute.project_fumo_backend.domain.enteties.user.Permission;
import com.sakute.project_fumo_backend.domain.enteties.user.Tag;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CreatorProfileMapper {

    public UserSummaryDTO toUserSummary(User user) {
        Set<String> tagNames = user.getUserProfile() != null
            ? user.getUserProfile().getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toSet())
            : Set.of();

        return new UserSummaryDTO(
            user.getUserId(),
            user.getUsername(),
            user.getProfilePicture(),
            user.getBio(),
            tagNames,
            user.getPermissions().contains(Permission.CAN_FUNDRAISE),
            user.getPermissions().contains(Permission.CAN_SELL_IP)
        );
    }

    public PostSummaryDTO toPostSummary(UserPost post) {
        return new PostSummaryDTO(
            post.getUserPostId(),
            post.getPostHeader(),
            post.getPostDescription(),
            post.getPhoto(),
            post.getCreatedAt(),
            post.getLikes().size(),
            post.getComments().size()
        );
    }

    public FundraisingSummaryDTO toFundraisingSummary(Fundraising f) {
        return new FundraisingSummaryDTO(
            f.getId(),
            f.getTitle(),
            f.getDescription(),
            f.getGoalAmount(),
            f.getCurrentAmount(), // наш обчислюваний метод
            f.getEndDate(),
            f.getStatus()
        );
    }

    public IpSummaryDTO toIpSummary(IntellectualProperty ip) {
        return new IpSummaryDTO(
            ip.getIpId(),
            ip.getName(),
            ip.getDescription(),
            ip.getTypeIp(),
            ip.getStatus()
        );
    }
}