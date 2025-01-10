package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.FundraisingLikes;
import com.sakute.project_fumo_backend.domain.enteties.User;
import com.sakute.project_fumo_backend.repository.RepositoryFactory;

import java.util.Optional;
import java.util.UUID;

public interface FundraisingLikesRepository  extends RepositoryFactory<FundraisingLikes, Long> {
    Optional<FundraisingLikes> findByLikeId(Long likeId);
    Optional<FundraisingLikes> findByFundraisingId(UUID fundraisingId);
    Optional<FundraisingLikes> findByUserId(User userId);
}
