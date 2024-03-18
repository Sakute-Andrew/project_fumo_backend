package com.sakute.project_fumo_backend.repository.repository.impl;

import com.sakute.project_fumo_backend.domain.enteties.IntellectualPropertyComments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IntellectualPropertyCommentsRepository extends JpaRepository<IntellectualPropertyComments, Long> {
    Optional<IntellectualPropertyComments> findByCommentId(Long commentId);
    Optional<IntellectualPropertyComments> findIntellectualPropertyCommentsByCommentId(Long intellectualPropertyId);
    Optional<IntellectualPropertyComments> findByUserId(Long userId);
}
