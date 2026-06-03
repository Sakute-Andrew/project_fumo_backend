package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.post.UserPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.security.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserPostRepository extends JpaRepository<UserPost, UUID>, JpaSpecificationExecutor<UserPost> {

    Optional<UserPost> findByUserPostId(UUID userPostId);

    // Видалення за ID
    void deleteByUserPostId(UUID userPostId);

    // Перевірка існування за ID
    boolean existsByUserPostId(UUID userPostId);

    // Пошук за заголовком (точний збіг)
    Optional<UserPost> findByPostHeader(String postHeader);

    List<UserPost> findByAuthorUserIdOrderByCreatedAtDesc(UUID userId);

    // Пошук за частиною заголовка (LIKE запит)
    @Query("SELECT p FROM UserPost p WHERE LOWER(p.postHeader) LIKE LOWER(CONCAT('%', :header, '%'))")
    Page<UserPost> findByPostHeaderContainingIgnoreCase(@Param("header") String header, Pageable pageable);

    // Пошук за автором (через User entity)
    @Query("SELECT p FROM UserPost p WHERE p.author.userId = :userId")
    Page<UserPost> findByUserId(@Param("userId") UUID userId, Pageable pageable);

    // Отримання останніх N постів для головної сторінки
    @Query("SELECT p FROM UserPost p ORDER BY p.createdAt DESC")
    List<UserPost> findLatestPosts(Pageable pageable);

    // Перевірка власника через username
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM UserPost p " +
            "WHERE p.userPostId = :postId AND p.author.username = :username")
    boolean isPostOwnerByUsername(@Param("postId") UUID postId, @Param("username") String username);

    // Пошук за назвою топіку (якщо PostTagTopic має поле name)
    @Query("SELECT p FROM UserPost p WHERE LOWER(p.topic.postName) = LOWER(:topicName)")
    Page<UserPost> findByTopicName(@Param("topicName") String topicName, Pageable pageable);

}



