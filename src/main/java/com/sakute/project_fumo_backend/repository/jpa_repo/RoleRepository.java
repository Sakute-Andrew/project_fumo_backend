package com.sakute.project_fumo_backend.repository.jpa_repo;

import com.sakute.project_fumo_backend.domain.enteties.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository  extends JpaRepository<Role, Long> {

}
