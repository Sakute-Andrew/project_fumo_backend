package com.sakute.project_fumo_backend.domain.service;

import com.sakute.project_fumo_backend.domain.Service;
import com.sakute.project_fumo_backend.domain.enteties.User;

public interface UserService extends Service<User> {
    /**
     * @param user
     * @return
     */
    @Override
    User save(User user);

    /**
     * @param user
     * @return
     */
    @Override
    User update(User user);

    /**
     * @param user
     */
    @Override
    void delete(User user);

    /**
     * @param id
     * @return
     */
    @Override
    User findById(Long id);
}
