package com.sakute.project_fumo_backend.infrastructure.impl;

import com.sakute.project_fumo_backend.domain.enteties.User;
import com.sakute.project_fumo_backend.infrastructure.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface UserRepository extends JpaRepository<, BigInteger> {
}
