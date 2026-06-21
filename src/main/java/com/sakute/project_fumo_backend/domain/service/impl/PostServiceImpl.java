package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.controller.exception.NotFoundException;
import com.sakute.project_fumo_backend.domain.ServiceGeneric;
import com.sakute.project_fumo_backend.domain.dto.post.PostMapper;
import com.sakute.project_fumo_backend.domain.dto.post.PostTopicDto;
import com.sakute.project_fumo_backend.domain.dto.post.UserPostDto;
import com.sakute.project_fumo_backend.domain.enteties.post.PostTagTopic;
import com.sakute.project_fumo_backend.domain.enteties.post.UserPost;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.service.FileService;
import com.sakute.project_fumo_backend.domain.service.PostService;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.PostTagTopicRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class PostServiceImpl extends ServiceGeneric<UserPost, UUID> implements PostService  {

    private final UserPostRepository postRepository;
    private final PostMapper postMapper;
    private final PostTagTopicRepository postTagTopicRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    @Autowired
    public PostServiceImpl(UserRepository userRepository, UserPostRepository postRepository, PostMapper postMapper, PostTagTopicRepository postTagTopicRepository, FileService fileService) {
        super(postRepository);
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.postTagTopicRepository = postTagTopicRepository;
        this.userRepository = userRepository;
        this.fileService = fileService;
    }

    @Transactional(readOnly = true)
    public UserPostDto findPostById(UUID id) {
        return postMapper.toDto(postRepository.findByUserPostId(id)
                .orElseThrow(() -> new NotFoundException("Пост з ID " + id + " не знайдено")));
    }

    @Transactional(readOnly = true)
    public UserPostDto findByTitle(String title) {
        return postMapper.toDto(postRepository.findByPostHeader(title)
                .orElseThrow(() -> new NotFoundException("Пост з заголовком \"" + title + "\" не знайдено")));
    }

    @Transactional(readOnly = true)
    public Page<UserPostDto> findAll(String name, Long topicId, String period, Pageable pageable) {
        Specification<UserPost> spec = Specification
                .where(byName(name))
                .and(byTopic(topicId))
                .and(byPeriod(period));

        Page<UserPost> page = postRepository.findAll(spec, pageable);

        // Маппимо ТУТ, поки транзакція відкрита
        List<UserPostDto> dtos = page.getContent()
                .stream()
                .map(postMapper::toDto)
                .toList();

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    private Specification<UserPost> byName(String name) {
        return (name != null && !name.trim().isEmpty())
                ? (root, query, cb) -> cb.like(cb.lower(root.get("postHeader")), "%" + name.toLowerCase() + "%")
                : null;
    }

    private Specification<UserPost> byTopic(Long topicId) {
        return topicId != null
                ? (root, query, cb) -> cb.equal(root.get("topic").get("postTopicId"), topicId)
                : null;
    }

    private Specification<UserPost> byPeriod(String period) {
        if (period == null || period.isBlank() || period.equalsIgnoreCase("all")) return null;
        Instant from = switch (period.toLowerCase()) {
            case "today" -> Instant.now().truncatedTo(ChronoUnit.DAYS);
            case "week"  -> Instant.now().minus(7, ChronoUnit.DAYS);
            case "month" -> Instant.now().minus(30, ChronoUnit.DAYS);
            default      -> null;
        };
        if (from == null) return null;
        Timestamp ts = Timestamp.from(from);
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), ts);
    }

    @Override
    public List<PostTopicDto> getPostTagTopic() {
        return postTagTopicRepository.findAll()
                .stream()
                .map(t -> new PostTopicDto(t.getPostTopicId(), t.getPostName()))
                .toList();
    }


    @Transactional
    // Оновлення поста
    public UserPostDto update(UUID id,UserPostDto post) {
        UserPost existingPost = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пост не знайдено"));
        postMapper.updateEntityFromDto(post, existingPost);

        // встановлюємо topic вручну
        if (post.getPostTopic() != null && post.getPostTopic().getPostTopicId() != null) {
            PostTagTopic topic = postTagTopicRepository.findById(post.getPostTopic().getPostTopicId())
                    .orElseThrow(() -> new NotFoundException("Topic не знайдено"));
            existingPost.setTopic(topic);
        }

        return postMapper.toDto(postRepository.save(existingPost));
        }
    @Transactional
    public void deleteById(UUID id) {
        UserPost post = postRepository.findByUserPostId(id)
                .orElseThrow(() -> new NotFoundException("Пост з ID " + id + " не знайдено"));
        String photo = post.getPhoto();
        postRepository.delete(post);
        fileService.deleteFile(photo);
    }

    public boolean isPostOwner(UUID postId, String username) {
        return postRepository.isPostOwnerByUsername(postId, username);
    }

    // Збереження нового поста
    public UserPostDto savePost(UserPostDto userPostDto) {
        UserPost userPost = postMapper.toEntity(userPostDto);
        userPost.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Користувача не знайдено: " + username));
        userPost.setAuthor(author);


        UserPost saved = postRepository.save(userPost);
        return postMapper.toDto(saved);
    }

}
