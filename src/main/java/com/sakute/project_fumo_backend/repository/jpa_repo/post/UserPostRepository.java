package com.sakute.project_fumo_backend.repository.jpa_repo.post;

import com.sakute.project_fumo_backend.domain.enteties.post.UserPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.security.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserPostRepository extends JpaRepository<UserPost, UUID> {

    @Query(value = "SELECT * FROM user_post", nativeQuery = true)
    List<UserPost> findAllBy();

    List<UserPost> findUserPostByPostHeader(String postHeader);

    Optional<UserPost> findByUserPostId(UUID userPostId);

    // Видалення за ID
    void deleteByUserPostId(UUID userPostId);

    // Перевірка існування за ID
    boolean existsByUserPostId(UUID userPostId);

    // Пошук за заголовком (точний збіг)
    Optional<UserPost> findByPostHeader(String postHeader);

    // Пошук за заголовком з ігноруванням регістру
    Optional<UserPost> findByPostHeaderIgnoreCase(String postHeader);

    // Пошук за частиною заголовка (LIKE запит)
    @Query("SELECT p FROM UserPost p WHERE LOWER(p.postHeader) LIKE LOWER(CONCAT('%', :header, '%'))")
    Page<UserPost> findByPostHeaderContainingIgnoreCase(@Param("header") String header, Pageable pageable);

    // Альтернативний варіант пошуку за частиною заголовка
    Page<UserPost> findByPostHeaderContainingIgnoreCaseOrderByCreatedAtDesc(String header, Pageable pageable);

    // Пошук за автором (через User entity)
    @Query("SELECT p FROM UserPost p WHERE p.userId.userId = :userId")
    Page<UserPost> findByUserId(@Param("userId") UUID userId, Pageable pageable);

    // Пошук за автором з сортуванням
    @Query("SELECT p FROM UserPost p WHERE p.userId.userId = :userId ORDER BY p.createdAt DESC")
    Page<UserPost> findByUserIdOrderByCreatedAtDesc(@Param("userId") UUID userId, Pageable pageable);

    // Пошук за топіком/категорією
    @Query("SELECT p FROM UserPost p WHERE p.postTagTopic.postTopicId = :topicId")
    Page<UserPost> findByPostTagTopicId(@Param("topicId") UUID topicId, Pageable pageable);

    // Пошук за топіком з сортуванням
    @Query("SELECT p FROM UserPost p WHERE p.postTagTopic.postTopicId = :topicId ORDER BY p.createdAt DESC")
    Page<UserPost> findByPostTagTopicIdOrderByCreatedAtDesc(@Param("topicId") UUID topicId, Pageable pageable);

    // Комбінований пошук за заголовком та топіком
    @Query("SELECT p FROM UserPost p WHERE " +
            "(:header IS NULL OR LOWER(p.postHeader) LIKE LOWER(CONCAT('%', :header, '%'))) AND " +
            "(:topicId IS NULL OR p.postTagTopic.postTopicId = :topicId)")
    Page<UserPost> findByHeaderAndTopic(@Param("header") String header,
                                        @Param("topicId") UUID topicId,
                                        Pageable pageable);

    // Пошук з повнотекстовим пошуком (за заголовком та описом)
    @Query("SELECT p FROM UserPost p WHERE " +
            "LOWER(p.postHeader) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.postDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<UserPost> searchByHeaderOrDescription(@Param("searchTerm") String searchTerm, Pageable pageable);

    // Отримання останніх N постів для головної сторінки
    @Query("SELECT p FROM UserPost p ORDER BY p.createdAt DESC")
    List<UserPost> findLatestPosts(Pageable pageable);

    // Перевірка, чи є користувач автором поста
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM UserPost p " +
            "WHERE p.userPostId = :postId AND p.userId.userId = :userId")
    boolean isPostOwner(@Param("postId") UUID postId, @Param("userId") UUID userId);

    // Перевірка власника через username
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM UserPost p " +
            "WHERE p.userPostId = :postId AND p.userId.username = :username")
    boolean isPostOwnerByUsername(@Param("postId") UUID postId, @Param("username") String username);

    // Підрахунок постів користувача
    @Query("SELECT COUNT(p) FROM UserPost p WHERE p.userId.userId = :userId")
    long countByUserId(@Param("userId") UUID userId);

    // Пошук постів, створених за певний період
    @Query("SELECT p FROM UserPost p WHERE p.createdAt >= :startDate AND p.createdAt <= :endDate")
    Page<UserPost> findByDateRange(@Param("startDate") Timestamp startDate,
                                   @Param("endDate") Timestamp endDate,
                                   Pageable pageable);

    // Пошук постів з фото
    @Query("SELECT p FROM UserPost p WHERE p.photo IS NOT NULL AND p.photo != ''")
    Page<UserPost> findPostsWithPhotos(Pageable pageable);

    // Пошук постів без фото
    @Query("SELECT p FROM UserPost p WHERE p.photo IS NULL OR p.photo = ''")
    Page<UserPost> findPostsWithoutPhotos(Pageable pageable);

    // Отримання рандомних постів для рекомендацій
    @Query(value = "SELECT * FROM user_post ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<UserPost> findRandomPosts(@Param("limit") int limit);

    // Для MySQL використовуй RAND() замість RANDOM():
    // @Query(value = "SELECT * FROM user_post ORDER BY RAND() LIMIT :limit", nativeQuery = true)

    // Пошук за назвою топіку (якщо PostTagTopic має поле name)
    @Query("SELECT p FROM UserPost p WHERE LOWER(p.postTagTopic.postName) = LOWER(:topicName)")
    Page<UserPost> findByTopicName(@Param("topicName") String topicName, Pageable pageable);

    // Комбінований запит для адміністративних потреб
    @Query("SELECT p FROM UserPost p WHERE " +
            "(:userId IS NULL OR p.userId.userId = :userId) AND " +
            "(:topicId IS NULL OR p.postTagTopic.postTopicId = :topicId) AND " +
            "(:hasPhoto IS NULL OR (:hasPhoto = true AND p.photo IS NOT NULL AND p.photo != '') OR (:hasPhoto = false AND (p.photo IS NULL OR p.photo = '')))")
    Page<UserPost> findWithFilters(@Param("userId") UUID userId,
                                   @Param("topicId") UUID topicId,
                                   @Param("hasPhoto") Boolean hasPhoto,
                                   Pageable pageable);

    // Пошук останніх постів конкретного користувача
    @Query("SELECT p FROM UserPost p WHERE p.userId.userId = :userId ORDER BY p.createdAt DESC")
    List<UserPost> findLatestPostsByUserId(@Param("userId") UUID userId, Pageable pageable);

    // Пошук постів за користувачем та топіком
    @Query("SELECT p FROM UserPost p WHERE p.userId.userId = :userId AND p.postTagTopic.postTopicId = :topicId")
    Page<UserPost> findByUserIdAndTopicId(@Param("userId") UUID userId,
                                          @Param("topicId") UUID topicId,
                                          Pageable pageable);
}



