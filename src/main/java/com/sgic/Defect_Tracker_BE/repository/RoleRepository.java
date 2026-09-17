package com.sgic.Defect_Tracker_BE.repository;

import com.sgic.Defect_Tracker_BE.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleNameIgnoreCase(String roleName);
    boolean existsByRoleNameIgnoreCase(String roleName);
    boolean existsByRoleNameIgnoreCaseAndIdNot(String roleName, Long id);
}
