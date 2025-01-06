package com.sakute.project_fumo_backend.domain.service.impl;

import com.sakute.project_fumo_backend.domain.ServiceGeneric;
import com.sakute.project_fumo_backend.domain.enteties.UserPost;
import com.sakute.project_fumo_backend.domain.service.PostService;
import com.sakute.project_fumo_backend.repository.jpa_repo.UserPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PostServiceImpl extends ServiceGeneric<UserPost, UUID> implements PostService  {

    private final UserPostRepository userPostRepository;

    @Autowired
    public PostServiceImpl(UserPostRepository userPostRepository) {
        super(userPostRepository);
        this.userPostRepository = userPostRepository;
    }

}
