package com.sakute.project_fumo_backend.domain.service;

import com.sakute.project_fumo_backend.domain.Service;
import com.sakute.project_fumo_backend.domain.enteties.UserPost;

import java.util.List;
import java.util.UUID;

public interface PostService extends Service<UserPost, UUID> {
    /**
     * @param userPost
     * @return
     */
    @Override
    UserPost save(UserPost userPost);

    /**
     * @param userPost
     * @return
     */
    @Override
    UserPost update(UserPost userPost);

    /**
     * @param userPost
     */
    @Override
    void delete(UserPost userPost);

    /**
     * @param id
     * @return
     */
    @Override
    UserPost findById(UUID id);

    /**
     * @return
     */
    @Override
    List<UserPost> findAll();

    /**
     * @param query
     * @return
     */
    @Override
    List<UserPost> findAllByQuery(String query);

    /**
     * @param query
     * @param page
     * @param size
     * @return
     */
    @Override
    List<UserPost> findAllByQuery(String query, int page, int size);
}
