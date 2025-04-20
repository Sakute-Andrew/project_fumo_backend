package com.sakute.project_fumo_backend;

import com.github.javafaker.Faker;
import com.sakute.project_fumo_backend.domain.enteties.Comment;
import com.sakute.project_fumo_backend.domain.enteties.post.UserPost;
import com.sakute.project_fumo_backend.domain.enteties.user.User;
import com.sakute.project_fumo_backend.domain.service.PostService;
import com.sakute.project_fumo_backend.domain.service.UserService;
import com.sakute.project_fumo_backend.domain.service.dto.CommentDto;
import com.sakute.project_fumo_backend.domain.service.dto.mapper.CommentDtoMapper;
import com.sakute.project_fumo_backend.domain.service.impl.PostServiceImpl;
import com.sakute.project_fumo_backend.domain.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.Console;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectFumoBackendApplicationTests {


    @Autowired
    PostServiceImpl postService;


    @Test
    @DisplayName("Mapping Comment to Dto")
    public void commentMappingToDto() {
        Comment comment = new Comment();
        Faker faker = new Faker();

        comment.setCommentId(1L);
        comment.setContent(faker.ancient().primordial());
        comment.setUserId(new User());
        comment.setUserPostId(UUID.randomUUID());
        comment.setCreatedAt(Timestamp.from(Instant.now()));

        System.out.println(comment);
        CommentDto commentDto = CommentDtoMapper.INSTANCE.mapToDto(comment);

        System.out.println(commentDto);
    }

    @Test
    @DisplayName("Creating users post")
    public void createUsersPost() {
        for (int i = 0; i < 5; i++) {
            UserPost userPost = new UserPost();
            Faker faker = new Faker();
            String uuid = "000b654d-bddf-4423-8683-7ad8c0c83718";



            userPost.setUserId(new User(UUID.fromString(uuid), "sakute", "orpha.barrows@gmail.com", 1L, null, null));
            userPost.setUserPostId(UUID.randomUUID());
            userPost.setCreatedAt(Timestamp.from(Instant.now()));
            userPost.setPostTopic(1L);
            userPost.setPostHeader(faker.harryPotter().book());
            userPost.setPostDescription(faker.harryPotter().quote());

            postService.save(userPost);
        }

    }

}
