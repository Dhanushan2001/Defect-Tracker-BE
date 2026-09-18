package com.sgic.Defect_Tracker_BE.repository;

import com.sgic.Defect_Tracker_BE.entity.SubModuleDeveloper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubModuleDeveloperRepository extends JpaRepository<SubModuleDeveloper, Long> {

    List<SubModuleDeveloper> findBySubModuleIdAndIsActiveTrue(Long subModuleId);

    Optional<SubModuleDeveloper> findBySubModuleIdAndEmployeeIdAndIsActiveTrue(Long subModuleId, Long employeeId);

    List<SubModuleDeveloper> findByProjectIdAndIsActiveTrue(Long projectId);

    List<SubModuleDeveloper> findByModuleIdAndIsActiveTrue(Long moduleId);

    List<SubModuleDeveloper> findByEmployeeIdAndIsActiveTrue(Long employeeId);

    boolean existsBySubModuleIdAndEmployeeIdAndIsActiveTrue(Long subModuleId, Long employeeId);
}
