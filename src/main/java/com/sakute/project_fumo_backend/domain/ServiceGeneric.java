package com.sakute.project_fumo_backend.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class ServiceGeneric<T, ID> implements Service<T, ID> {

    protected final JpaRepository<T, ID> repository;

    protected ServiceGeneric(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public T save(T t) {
        return repository.save(t);
    }

    @Override
    public T update(T t) {
        return repository.save(t);
    }

    @Override
    public void delete(T t) {
        repository.delete(t);
    }

    @Override
    public T findById(ID id) {
        Optional<T> entity = repository.findById(id);
        return entity.orElse(null);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public List<T> findAllByQuery(String query) {
        throw new UnsupportedOperationException("Цей метод потребує специфічної реалізації.");
    }

    @Override
    public List<T> findAllByQuery(String query, int page, int size) {
        throw new UnsupportedOperationException("Цей метод потребує специфічної реалізації.");
    }
}
