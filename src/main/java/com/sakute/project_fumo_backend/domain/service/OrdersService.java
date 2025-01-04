package com.sakute.project_fumo_backend.domain.service;

import com.sakute.project_fumo_backend.domain.Service;
import com.sakute.project_fumo_backend.domain.enteties.Orders;

import java.util.List;
import java.util.UUID;

public interface OrdersService extends Service<Orders, Long> {
    /**
     * @param orders
     * @return
     */
    @Override
    Orders save(Orders orders);

    /**
     * @param orders
     * @return
     */
    @Override
    Orders update(Orders orders);

    /**
     * @param orders
     */
    @Override
    void delete(Orders orders);

    /**
     * @param id
     * @return
     */
    @Override
    Orders findById(Long id);

    /**
     * @return
     */
    @Override
    List<Orders> findAll();

    /**
     * @param query
     * @return
     */
    @Override
    List<Orders> findAllByQuery(String query);

    /**
     * @param query
     * @param page
     * @param size
     * @return
     */
    @Override
    List<Orders> findAllByQuery(String query, int page, int size);
}
