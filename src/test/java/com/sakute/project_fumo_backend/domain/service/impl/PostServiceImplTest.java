package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.controller.exception.NotFoundException;
import com.sakute.project_fumo_backend.domain.dto.post.PostMapper;
import com.sakute.project_fumo_backend.domain.dto.post.UserPostDto;
import com.sakute.project_fumo_backend.domain.enteties.post.UserPost;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.service.FileService;
import com.sakute.project_fumo_backend.repository.jpa_repo.PostTagTopicRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserPostRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private UserPostRepository postRepository;

    @Mock
    private PostMapper postMapper;

    @Mock
    private PostTagTopicRepository postTagTopicRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FileService fileService;

    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        postService = new PostServiceImpl(userRepository, postRepository, postMapper, postTagTopicRepository, fileService);
    }

    // -------------------------------------------------------
    // findPostById
    // -------------------------------------------------------

    @Test
    void findPostById_shouldReturnDto_whenPostExists() {
        UUID id = UUID.randomUUID();
        UserPost post = new UserPost();
        UserPostDto expected = new UserPostDto();

        when(postRepository.findByUserPostId(id)).thenReturn(Optional.of(post));
        when(postMapper.toDto(post)).thenReturn(expected);

        UserPostDto result = postService.findPostById(id);

        assertThat(result).isEqualTo(expected);
        verify(postRepository).findByUserPostId(id);
    }

    @Test
    void findPostById_shouldThrow_whenPostNotFound() {
        UUID id = UUID.randomUUID();
        when(postRepository.findByUserPostId(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.findPostById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    // -------------------------------------------------------
    // findByTitle
    // -------------------------------------------------------

    @Test
    void findByTitle_shouldReturnDto_whenTitleExists() {
        String title = "Тестовий заголовок";
        UserPost post = new UserPost();
        UserPostDto expected = new UserPostDto();

        when(postRepository.findByPostHeader(title)).thenReturn(Optional.of(post));
        when(postMapper.toDto(post)).thenReturn(expected);

        UserPostDto result = postService.findByTitle(title);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void findByTitle_shouldThrow_whenTitleNotFound() {
        String title = "Неіснуючий заголовок";
        when(postRepository.findByPostHeader(title)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.findByTitle(title))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(title);
    }

    // -------------------------------------------------------
    // deleteById
    // -------------------------------------------------------

    @Test
    void deleteById_shouldCallDelete_whenPostExists() {
        UUID id = UUID.randomUUID();
        UserPost post = new UserPost();
        post.setPhoto("/api/v1/files/posts/photo.jpg");

        when(postRepository.findByUserPostId(id)).thenReturn(Optional.of(post));

        postService.deleteById(id);

        verify(postRepository).delete(post);
        verify(fileService).deleteFile("/api/v1/files/posts/photo.jpg");
    }

    @Test
    void deleteById_shouldThrow_whenPostNotFound() {
        UUID id = UUID.randomUUID();
        when(postRepository.findByUserPostId(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.deleteById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    // -------------------------------------------------------
    // savePost
    // -------------------------------------------------------

    @Test
    void savePost_shouldSetAuthorAndReturnDto() {
        UserPostDto inputDto = new UserPostDto();
        UserPost entity = new UserPost();
        UserPost saved = new UserPost();
        UserPostDto expectedDto = new UserPostDto();
        User user = new User();

        when(postMapper.toEntity(inputDto)).thenReturn(entity);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(postRepository.save(entity)).thenReturn(saved);
        when(postMapper.toDto(saved)).thenReturn(expectedDto);

        mockSecurityContext("testuser");

        UserPostDto result = postService.savePost(inputDto);

        assertThat(result).isEqualTo(expectedDto);
        assertThat(entity.getAuthor()).isEqualTo(user);
        verify(postRepository).save(entity);
    }

    @Test
    void savePost_shouldThrow_whenUserNotFound() {
        UserPostDto inputDto = new UserPostDto();
        UserPost entity = new UserPost();

        when(postMapper.toEntity(inputDto)).thenReturn(entity);
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        mockSecurityContext("ghost");

        assertThatThrownBy(() -> postService.savePost(inputDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("ghost");
    }

    // -------------------------------------------------------
    // Хелпер для мока SecurityContext
    // -------------------------------------------------------

    private void mockSecurityContext(String username) {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(username);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);

        SecurityContextHolder.setContext(context);
    }
}