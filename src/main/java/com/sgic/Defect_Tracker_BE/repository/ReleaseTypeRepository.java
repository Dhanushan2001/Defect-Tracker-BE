package com.sgic.Defect_Tracker_BE.repository;

import com.sgic.Defect_Tracker_BE.entity.ReleaseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReleaseTypeRepository extends JpaRepository<ReleaseType, Long> {
    Optional<ReleaseType> findByReleaseTypeNameIgnoreCase(String releaseTypeName);
    boolean existsByReleaseTypeNameIgnoreCase(String releaseTypeName);
    boolean existsByReleaseTypeNameIgnoreCaseAndIdNot(String releaseTypeName, Long id);
}
