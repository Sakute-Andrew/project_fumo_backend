package com.sakute.project_fumo_backend.repository.jpa_repo.fundraising;

import com.sakute.project_fumo_backend.domain.enteties.fundraising.FundraisingGoals;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FundraisingGoalsRepository  extends JpaRepository<FundraisingGoals, Long> {
    Optional<FundraisingGoals> findByFundraising(FundraisingGoals fundraisingId);
    Optional<FundraisingGoals> findFundraisingGoalsByGoalId(Long goalId);
}
