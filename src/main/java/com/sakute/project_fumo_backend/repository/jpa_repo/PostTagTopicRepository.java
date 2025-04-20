package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.post.PostTagTopic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostTagTopicRepository  extends JpaRepository<PostTagTopic, Long> {
    Optional<PostTagTopic> findByPostTopicId(Long postTagId);
    Optional<PostTagTopic> findByPostName(String name);
}
