package com.sakute.project_fumo_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryFactory<T, ID> extends JpaRepository<T, ID> {

    Optional<T> findByName(String name);

}
