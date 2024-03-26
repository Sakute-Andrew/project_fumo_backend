package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.FundraisingUpdates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FundraisingUpdatesRepository  extends JpaRepository<FundraisingUpdates, Long> {
    Optional<FundraisingUpdates> findByUpdateId(Long updateId);
    Optional<FundraisingUpdates> findByFundraisingId(UUID fundraisingId);

}
