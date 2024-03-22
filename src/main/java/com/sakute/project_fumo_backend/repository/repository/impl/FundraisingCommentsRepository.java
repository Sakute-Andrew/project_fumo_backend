package com.sakute.project_fumo_backend.repository.repository.impl;

import com.sakute.project_fumo_backend.domain.enteties.FundraisingComments;
import com.sakute.project_fumo_backend.domain.enteties.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FundraisingCommentsRepository extends JpaRepository<FundraisingComments, Long>{
    Optional<FundraisingComments> findByCommentId(Long commentId);
    Optional<FundraisingComments> findByFundraisingId(Long fundraisingId);
    Optional<FundraisingComments> findByUserId(User userId);
}
