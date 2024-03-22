package com.sakute.project_fumo_backend.repository.repository.impl;

import com.sakute.project_fumo_backend.domain.enteties.User;
import com.sakute.project_fumo_backend.domain.enteties.UserPost;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserPostRepository extends JpaRepository<UserPost, Long> {
    Optional<UserPost> findUserPostByUserPostId(Long userPostId);
    Optional<UserPost> findUserPostByUserPostIdAndUserId(Long userPostId, User userId);
    Optional<UserPost> findUserPostsByUserId(User userId);
    List<UserPost> findAllBy();

}
