package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.domain.dto.user_profile.*;
import com.sakute.project_fumo_backend.domain.enteties.fundraising.Fundraising;
import com.sakute.project_fumo_backend.domain.enteties.intprop.IpStatus;
import com.sakute.project_fumo_backend.domain.enteties.user.Permission;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.FundraisingRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.IntellectualPropertyRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserPostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreatorProfileService {

    private final UserRepository userRepository;
    private final UserPostRepository postRepository;
    private final FundraisingRepository fundraisingRepository;
    private final IntellectualPropertyRepository ipRepository;
    private final CreatorProfileMapper mapper;

    @Transactional(readOnly = true)
    public CreatorProfileDTO getProfile(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException(
                "User not found: " + userId
            ));

        List<PostSummaryDTO> posts = postRepository
            .findByAuthorUserIdOrderByCreatedAtDesc(userId)
            .stream()
            .map(mapper::toPostSummary)
            .toList();

        List<FundraisingSummaryDTO> fundraisings = user.getPermissions()
            .contains(Permission.CAN_FUNDRAISE)
                ? fundraisingRepository
                    .findByOwnerUserIdAndStatus(userId, Fundraising.Status.ACTIVE)
                    .stream()
                    .map(mapper::toFundraisingSummary)
                    .toList()
                : List.of();

        List<IpSummaryDTO> ips = user.getPermissions()
            .contains(Permission.CAN_SELL_IP)
                ? ipRepository
                    .findByOwnerUserIdAndStatus(userId, IpStatus.AVAILABLE)
                    .stream()
                    .map(mapper::toIpSummary)
                    .toList()
                : List.of();

        return new CreatorProfileDTO(
            mapper.toUserSummary(user),
            posts,
            fundraisings,
            ips
        );
    }
}