package com.sgic.Defect_Tracker_BE.repository;

import com.sgic.Defect_Tracker_BE.entity.DefectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DefectTypeRepository extends JpaRepository<DefectType, Long> {
    Optional<DefectType> findByDefectTypeNameIgnoreCase(String defectTypeName);
    boolean existsByDefectTypeNameIgnoreCase(String defectTypeName);
    boolean existsByDefectTypeNameIgnoreCaseAndIdNot(String defectTypeName, Long id);
}
