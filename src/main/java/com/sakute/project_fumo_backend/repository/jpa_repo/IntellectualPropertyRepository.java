package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.IntellectualProperty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IntellectualPropertyRepository  extends JpaRepository<IntellectualProperty, UUID> {

}
