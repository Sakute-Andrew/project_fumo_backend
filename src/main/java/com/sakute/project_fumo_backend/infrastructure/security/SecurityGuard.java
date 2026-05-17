package com.sakute.project_fumo_backend.infrastructure.security;

import com.sakute.project_fumo_backend.repository.jpa_repo.CommentRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.FundraisingRepository;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("guard") // Називаємо його коротко
@RequiredArgsConstructor
public class SecurityGuard {

    private final FundraisingRepository fundraisingRepository;
    private final UserPostRepository postRepository;
    private final CommentRepository commentRepository;

    // 1. Перевірка для зборів
    public boolean isFundraisingOwner(UUID id, String email) {
        return fundraisingRepository.findById(id)
                .map(f -> f.getOwner().getEmail().equals(email))
                .orElse(false);
    }

    // 2. Перевірка для постів
    public boolean isPostOwner(UUID id, String email) {
        return postRepository.findById(id)
                .map(p -> p.getAuthor().getEmail().equals(email))
                .orElse(false);
    }

    // 3. Перевірка для коментарів
    public boolean isCommentOwner(Long id, String email) {
        return commentRepository.findById(id)
                .map(c -> c.getAuthor().getEmail().equals(email))
                .orElse(false);
    }
}
