package com.sakute.project_fumo_backend.controller.rest;

import com.sakute.project_fumo_backend.domain.enteties.post.PostTagTopic;
import com.sakute.project_fumo_backend.repository.jpa_repo.PostTagTopicRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/posts/topics")
@PreAuthorize("hasRole('ADMIN')")
public class PostTopicController {

    private final PostTagTopicRepository postTagTopicRepository;

    public PostTopicController(PostTagTopicRepository postTagTopicRepository) {
        this.postTagTopicRepository = postTagTopicRepository;
    }

    @PostMapping
    public ResponseEntity<PostTagTopic> create(@RequestBody PostTagTopic topic) {
        topic.setPostTopicId(null); // щоб не перезаписати існуючий
        return ResponseEntity.status(HttpStatus.CREATED).body(postTagTopicRepository.save(topic));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostTagTopic> update(@PathVariable Long id, @RequestBody PostTagTopic topic) {
        if (!postTagTopicRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        topic.setPostTopicId(id);
        return ResponseEntity.ok(postTagTopicRepository.save(topic));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!postTagTopicRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        postTagTopicRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}