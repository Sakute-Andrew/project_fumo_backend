package com.sakute.project_fumo_backend.domain.service;

import com.sakute.project_fumo_backend.domain.Service;
import com.sakute.project_fumo_backend.domain.enteties.Donations;

import java.util.List;

public interface DonationService extends Service<Donations> {
    /**
     * @param donations
     * @return
     */
    @Override
    Donations save(Donations donations);

    /**
     * @param donations
     * @return
     */
    @Override
    Donations update(Donations donations);

    /**
     * @param donations
     */
    @Override
    void delete(Donations donations);

    /**
     * @param id
     * @return
     */
    @Override
    Donations findById(Long id);

    /**
     * @return
     */
    @Override
    List<Donations> findAll();

    /**
     * @param query
     * @return
     */
    @Override
    List<Donations> findAllByQuery(String query);

    /**
     * @param query
     * @param page
     * @param size
     * @return
     */
    @Override
    List<Donations> findAllByQuery(String query, int page, int size);
}
