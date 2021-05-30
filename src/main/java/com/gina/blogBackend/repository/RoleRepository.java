package com.gina.blogBackend.repository;

import com.gina.blogBackend.model.ERole;
import com.gina.blogBackend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);
}
