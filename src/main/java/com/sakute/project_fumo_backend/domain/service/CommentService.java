package com.sakute.project_fumo_backend.domain.service;

import com.sakute.project_fumo_backend.domain.Service;
import com.sakute.project_fumo_backend.domain.enteties.Comment;

public interface CommentService extends Service<Comment>{
    /**
     * @param comment
     * @return
     */
    @Override
    Comment save(Comment comment);

    /**
     * @param comment
     * @return
     */
    @Override
    Comment update(Comment comment);

    /**
     * @param comment
     */
    @Override
    void delete(Comment comment);

    /**
     * @param id
     * @return
     */
    @Override
    Comment findById(Long id);
}
