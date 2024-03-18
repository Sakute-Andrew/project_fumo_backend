package com.sakute.project_fumo_backend.domain;

import java.util.List;

public interface Service<T>  {
    T save (T t);
    T update (T t);
    void delete (T t);
    T findById (Long id);
    List<T> findAll();
    List<T> findAllByQuery(String query);
    List<T> findAllByQuery(String query, int page, int size);

}
