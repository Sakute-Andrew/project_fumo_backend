package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.FundraisingComments;
import com.sakute.project_fumo_backend.domain.enteties.User;
import com.sakute.project_fumo_backend.repository.RepositoryFactory;

import java.util.Optional;
import java.util.UUID;

public interface FundraisingCommentsRepository extends RepositoryFactory<FundraisingComments, Long> {
    Optional<FundraisingComments> findByCommentId(Long commentId);
    Optional<FundraisingComments> findByFundraisingId(UUID fundraisingId);
    Optional<FundraisingComments> findByUserId(User userId);
}
