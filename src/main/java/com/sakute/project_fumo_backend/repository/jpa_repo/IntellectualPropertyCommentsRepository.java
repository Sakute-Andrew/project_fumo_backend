package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.IntellectualPropertyComments;
import com.sakute.project_fumo_backend.domain.enteties.User;
import com.sakute.project_fumo_backend.repository.RepositoryFactory;

import java.util.Optional;

public interface IntellectualPropertyCommentsRepository extends RepositoryFactory<IntellectualPropertyComments, Long> {
    Optional<IntellectualPropertyComments> findByCommentId(Long commentId);
    Optional<IntellectualPropertyComments> findIntellectualPropertyCommentsByCommentId(Long intellectualPropertyId);
    Optional<IntellectualPropertyComments> findByUserId(User userId);
}
