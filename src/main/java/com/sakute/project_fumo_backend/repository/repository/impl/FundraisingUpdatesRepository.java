package com.sakute.project_fumo_backend.repository.repository.impl;

import com.sakute.project_fumo_backend.domain.enteties.FundraisingUpdates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FundraisingUpdatesRepository  extends JpaRepository<FundraisingUpdates, Long> {
    Optional<FundraisingUpdates> findByUpdateId(Long updateId);
    Optional<FundraisingUpdates> findByFundraisingId(Long fundraisingId);

}
