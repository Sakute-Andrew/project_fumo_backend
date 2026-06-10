package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.domain.service.DashboardService;
import com.sakute.project_fumo_backend.domain.dto.DashboardStatsDto;
import com.sakute.project_fumo_backend.domain.enteties.RequestStatus;
import com.sakute.project_fumo_backend.domain.enteties.fundraising.Fundraising;
import com.sakute.project_fumo_backend.domain.enteties.intprop.IpStatus;
import com.sakute.project_fumo_backend.repository.jpa_repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final FundraisingRepository fundraisingRepository;
    private final DonationRepository donationRepository;
    private final PayoutRequestRepository payoutRepository;
    private final PermissionRequestRepository permissionRepository;
    private final IntellectualPropertyRepository ipRepository;
    private final UserPostRepository postRepository;

    @Transactional(readOnly = true)
    public DashboardStatsDto getStats() {
        DashboardStatsDto stats = new DashboardStatsDto();

        stats.setTotalUsers(userRepository.count());

        stats.setTotalFundraisings(fundraisingRepository.count());
        stats.setActiveFundraisings(fundraisingRepository.countByStatus(Fundraising.Status.ACTIVE));
        stats.setCompletedFundraisings(fundraisingRepository.countByStatus(Fundraising.Status.COMPLETED));

        stats.setTotalDonationsCount(donationRepository.count());
        stats.setTotalDonatedAmount(donationRepository.sumAllDonations());
        stats.setPendingPayoutsCount(payoutRepository.countByStatus(RequestStatus.PENDING));
        stats.setPendingPayoutAmount(payoutRepository.sumAmountByStatus(RequestStatus.PENDING));

        stats.setTotalIpAssets(ipRepository.count());
        stats.setIpLegalTroublesCount(ipRepository.countByStatus(IpStatus.LEGAL_TROUBLES));

        stats.setTotalPosts(postRepository.count());

        stats.setPendingPermissionsCount(permissionRepository.countByStatus(RequestStatus.PENDING));

        return stats;
    }
}