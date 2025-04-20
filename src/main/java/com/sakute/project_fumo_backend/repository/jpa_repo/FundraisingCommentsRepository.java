package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.fundraising.FundraisingComments;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FundraisingCommentsRepository extends JpaRepository<FundraisingComments, Long>{
    Optional<FundraisingComments> findByCommentId(Long commentId);
    Optional<FundraisingComments> findByFundraisingId(UUID fundraisingId);
    Optional<FundraisingComments> findByUserId(User userId);
}
