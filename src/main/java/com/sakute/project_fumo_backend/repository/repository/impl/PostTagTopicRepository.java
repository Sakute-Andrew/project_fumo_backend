package com.sakute.project_fumo_backend.repository.repository.impl;

import com.sakute.project_fumo_backend.domain.enteties.PostTagTopic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostTagTopicRepository  extends JpaRepository<PostTagTopic, Long> {
    Optional<PostTagTopic> findByPostTopicId(Long postTagId);
    Optional<PostTagTopic> findByPostName(String name);
}
