package com.sgic.Defect_Tracker_BE.repository;

import com.sgic.Defect_Tracker_BE.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {

    List<TestCase> findBySubModuleIdOrderByIdAsc(Long subModuleId);

    List<TestCase> findByModuleIdOrderByIdAsc(Long moduleId);

    List<TestCase> findByProjectIdOrderByIdAsc(Long projectId);

    @Query("SELECT tc FROM TestCase tc WHERE tc.subModule.id = :subModuleId " +
           "AND (:description IS NULL OR LOWER(tc.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
           "AND (:defectTypeId IS NULL OR tc.defectType.id = :defectTypeId) " +
           "AND (:severityId IS NULL OR tc.severity.id = :severityId) " +
           "ORDER BY tc.id ASC")
    List<TestCase> searchTestCases(
            @Param("subModuleId") Long subModuleId,
            @Param("description") String description,
            @Param("defectTypeId") Long defectTypeId,
            @Param("severityId") Long severityId);
}
