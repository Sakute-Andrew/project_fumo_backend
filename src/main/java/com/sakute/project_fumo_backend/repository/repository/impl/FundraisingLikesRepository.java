package com.sakute.project_fumo_backend.repository.repository.impl;

import com.sakute.project_fumo_backend.domain.enteties.FundraisingLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FundraisingLikesRepository  extends JpaRepository<FundraisingLikes, Long> {
    Optional<FundraisingLikes> findByLikeId(Long likeId);
    Optional<FundraisingLikes> findByFundraisingId(Long fundraisingId);
    Optional<FundraisingLikes> findByUserId(Long userId);
}
