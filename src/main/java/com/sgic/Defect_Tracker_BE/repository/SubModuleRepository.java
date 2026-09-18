package com.sgic.Defect_Tracker_BE.repository;

import com.sgic.Defect_Tracker_BE.entity.SubModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubModuleRepository extends JpaRepository<SubModule, Long> {

    List<SubModule> findByModuleIdOrderByIdAsc(Long moduleId);

    List<SubModule> findByModuleId(Long moduleId);

    List<SubModule> findByProjectIdOrderByIdAsc(Long projectId);

    List<SubModule> findByModuleIdIn(List<Long> moduleIds);

    boolean existsByModuleIdAndNameIgnoreCase(Long moduleId, String name);

    boolean existsByModuleIdAndNameIgnoreCaseAndIdNot(Long moduleId, String name, Long id);
}
