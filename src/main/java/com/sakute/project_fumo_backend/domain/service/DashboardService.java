package com.sakute.project_fumo_backend.domain.service;

import com.sakute.project_fumo_backend.domain.dto.DashboardStatsDto;
import com.sakute.project_fumo_backend.domain.enteties.RequestStatus;
import com.sakute.project_fumo_backend.repository.jpa_repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardService {
    
    private final UserRepository userRepository;
    private final FundraisingRepository fundraisingRepository;
    private final DonationRepository donationRepository;
    private final PayoutRequestRepository payoutRepository;
    private final PermissionRequestRepository permissionRepository;

    @Transactional(readOnly = true)
    public DashboardStatsDto getStats() {
        DashboardStatsDto stats = new DashboardStatsDto();
        
        stats.setTotalUsers(userRepository.count());
        // Додай сюди інші виклики...
        stats.setPendingPayoutsCount(payoutRepository.countByStatus(RequestStatus.PENDING));
        stats.setPendingPermissionsCount(permissionRepository.countByStatus(RequestStatus.PENDING));
        
        return stats;
    }
}