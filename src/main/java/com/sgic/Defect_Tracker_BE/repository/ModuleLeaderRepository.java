package com.sgic.Defect_Tracker_BE.repository;

import com.sgic.Defect_Tracker_BE.entity.ModuleLeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleLeaderRepository extends JpaRepository<ModuleLeader, Long> {

    Optional<ModuleLeader> findByModuleIdAndIsActiveTrue(Long moduleId);

    List<ModuleLeader> findByProjectIdAndIsActiveTrue(Long projectId);

    List<ModuleLeader> findByEmployeeIdAndIsActiveTrue(Long employeeId);

    boolean existsByModuleIdAndIsActiveTrue(Long moduleId);
}
