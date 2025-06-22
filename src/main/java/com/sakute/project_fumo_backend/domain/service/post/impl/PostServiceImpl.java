package com.sakute.project_fumo_backend.domain.service.post.impl;

import com.sakute.project_fumo_backend.controller.exeption.NotFoundExeption;
import com.sakute.project_fumo_backend.domain.ServiceGeneric;
import com.sakute.project_fumo_backend.domain.enteties.dto.PaginatedResponseDto;
import com.sakute.project_fumo_backend.domain.enteties.dto.UserDto;
import com.sakute.project_fumo_backend.domain.enteties.dto.UserPostDto;
import com.sakute.project_fumo_backend.domain.enteties.dto.mapper.PostMapper;
import com.sakute.project_fumo_backend.domain.enteties.post.PostTagTopic;
import com.sakute.project_fumo_backend.domain.enteties.post.UserPost;
import com.sakute.project_fumo_backend.domain.service.UserService;
import com.sakute.project_fumo_backend.domain.service.post.PostService;
import com.sakute.project_fumo_backend.repository.jpa_repo.post.PostTagTopicRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.post.UserPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl extends ServiceGeneric<UserPost, UUID> implements PostService  {

    private final UserPostRepository postRepository;
    private final PostMapper postMapper;
    private final PostTagTopicRepository postTagTopicRepository;

    @Autowired
    public PostServiceImpl(UserPostRepository postRepository, PostMapper postMapper, PostTagTopicRepository postTagTopicRepository) {
        super(postRepository);
        this.postRepository = postRepository;

        this.postMapper = postMapper;
        this.postTagTopicRepository = postTagTopicRepository;
    }


    public UserPostDto findPostById(UUID id) {
        return postMapper.toDto(postRepository.findByUserPostId(id)
                .orElseThrow(() -> new NotFoundExeption("Пост з ID " + id + " не знайдено")));
    }

    // Пошук за заголовком
    public UserPostDto findByTitle(String title) {
        return postMapper.toDto(postRepository.findByPostHeader(title)
                .orElseThrow(() -> new NotFoundExeption("Пост з заголовком '" + title + "' не знайдено")));
    }

    // Новий метод: пошук за заголовком з пагінацією
    public PaginatedResponseDto<UserPostDto> findByTitle(String title, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit); // page - 1 тому що Spring використовує 0-індексацію

        // Припускаємо, що у вас є метод для пошуку за частиною заголовку
        Page<UserPost> userPostPage = postRepository.findByPostHeaderContainingIgnoreCase(title, pageable);

        List<UserPostDto> posts = userPostPage.getContent().stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());

        return new PaginatedResponseDto<>(
                posts,
                userPostPage.getTotalPages(),
                userPostPage.getTotalElements(),
                page,
                limit
        );
    }

    @Override
    public Page<UserPostDto> findAllPaginated(String title, int page, int limit) {
        return null;
    }

    // Оновлений метод: всі пости з пагінацією
    public PaginatedResponseDto<UserPostDto> findAllPaginated(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit); // page - 1 тому що Spring використовує 0-індексацію
        Page<UserPost> userPostPage = postRepository.findAll(pageable);

        List<UserPostDto> posts = userPostPage.getContent().stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());

        return new PaginatedResponseDto<>(
                posts,
                userPostPage.getTotalPages(),
                userPostPage.getTotalElements(),
                page,
                limit
        );
    }

    public Page<UserPostDto> searchByTitle(String title, Pageable pageable) {
        Page<UserPost> userPostPage = postRepository.findByPostHeaderContainingIgnoreCase(title, pageable);
        return postMapper.toDtoPage(userPostPage);
    }

    public Page<UserPostDto> findByCategory(String topicName, Pageable pageable) {
        Page<UserPost> userPostPage = postRepository.findByTopicName(topicName, pageable);
        return postMapper.toDtoPage(userPostPage);
    }

    public Page<UserPostDto> findByUserId(UUID userId, Pageable pageable) {
        Page<UserPost> userPostPage = postRepository.findByUserId(userId, pageable);
        return postMapper.toDtoPage(userPostPage);
    }

    public List<UserPostDto> findAllForExplore(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<UserPost> userPosts = postRepository.findLatestPosts(pageable); // має повертати List<UserPost>
        return postMapper.toDtoList(userPosts);
    }

    @Override
    public List<PostTagTopic> getPostTagTopic() {
        return postTagTopicRepository.findAll();
    }

    // Оновлення поста
    public UserPost update(UserPostDto post) {
        UserPost existingPost = findById(post.getId());
        postMapper.updateEntityFromDto(post, existingPost);
        return postRepository.save(existingPost);
    }

    // Видалення за ID
    public void deleteById(UUID id) {
        if (!postRepository.existsByUserPostId(id)) {
            throw new NotFoundExeption("Пост з ID " + id + " не знайдено");
        }
        postRepository.deleteByUserPostId(id);
    }

    // Перевірка власника
    public boolean isOwner(UUID postId, String username) {
        return postRepository.isPostOwnerByUsername(postId, username);
    }

    // Збереження нового поста
    public boolean savePost(UserPostDto userPostpost) {

        UserPost userPost = postMapper.toEntity(userPostpost);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        userPost.setCreatedAt(now);
        try {
            postRepository.save(userPost);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

}
