package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.enteties.post.UserPost;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserPostRepository extends JpaRepository<UserPost, UUID> {

    @Query(value = "SELECT * FROM user_post", nativeQuery = true)
    List<UserPost> findAllBy();

    List<UserPost> findUserPostByPostHeader(String postHeader);


}
