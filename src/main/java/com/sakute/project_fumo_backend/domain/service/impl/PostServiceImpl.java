package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.domain.ServiceGeneric;
import com.sakute.project_fumo_backend.domain.enteties.UserPost;
import com.sakute.project_fumo_backend.domain.service.PostService;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PostServiceImpl extends ServiceGeneric<UserPost, UUID> implements PostService  {

    private final UserPostRepository userPostRepository;

    @Autowired
    public PostServiceImpl(UserPostRepository userPostRepository) {
        super(userPostRepository);
        this.userPostRepository = userPostRepository;
    }

    /**
     * @param userPost
     * @return
     */
    @Override
    public UserPost save(UserPost userPost) {
        return userPostRepository.save(userPost);
    }

    /**
     * Service method of deletion user post
     * @param userPost
     * @return userPost
     */
    @Override
    public UserPost update(UserPost userPost) {
        return null;
    }

    /**
     * @param userPost
     */
    @Override
    public void delete(UserPost userPost) {
        userPostRepository.delete(userPost);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public UserPost findById(UUID id) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public List<UserPost> findAll() {
        return userPostRepository.findAllBy();
    }

    /**
     * @param query
     * @return
     */
    @Override
    public List<UserPost> findAllByQuery(String query) {
        return null;
    }

    /**
     * @param query
     * @param page
     * @param size
     * @return
     */
    @Override
    public List<UserPost> findAllByQuery(String query, int page, int size) {
        return null;
    }
}
