package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.PostTagTopic;
import com.sakute.project_fumo_backend.repository.RepositoryFactory;

import java.util.Optional;

public interface PostTagTopicRepository  extends RepositoryFactory<PostTagTopic, Long> {
    Optional<PostTagTopic> findByPostTopicId(Long postTagId);
    Optional<PostTagTopic> findByPostName(String name);
}
