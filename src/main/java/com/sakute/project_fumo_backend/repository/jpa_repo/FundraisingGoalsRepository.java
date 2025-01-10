package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.FundraisingGoals;
import com.sakute.project_fumo_backend.repository.RepositoryFactory;

import java.util.Optional;

public interface FundraisingGoalsRepository  extends RepositoryFactory<FundraisingGoals, Long> {
    Optional<FundraisingGoals> findByFundraising(FundraisingGoals fundraisingId);
    Optional<FundraisingGoals> findFundraisingGoalsByGoalId(Long goalId);
}
