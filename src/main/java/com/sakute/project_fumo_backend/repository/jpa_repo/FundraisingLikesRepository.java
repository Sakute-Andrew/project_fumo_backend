package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.fundraising.FundraisingLikes;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FundraisingLikesRepository  extends JpaRepository<FundraisingLikes, Long> {
    Optional<FundraisingLikes> findByLikeId(Long likeId);
    Optional<FundraisingLikes> findByFundraisingId(UUID fundraisingId);
    Optional<FundraisingLikes> findByUserId(User userId);
}
