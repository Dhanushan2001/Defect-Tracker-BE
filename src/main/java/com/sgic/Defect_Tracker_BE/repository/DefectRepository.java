package com.sgic.Defect_Tracker_BE.repository;

import com.sgic.Defect_Tracker_BE.entity.Defect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DefectRepository extends JpaRepository<Defect, Long> {

    List<Defect> findByProjectIdOrderByIdAsc(Long projectId);

    List<Defect> findByReleaseIdOrderByIdDesc(Long releaseId);

    List<Defect> findByTestCaseIdOrderByIdDesc(Long testCaseId);
 
    Optional<Defect> findTopByTestCaseIdOrderByIdDesc(Long testCaseId);

    Optional<Defect> findTopByReleaseIdAndTestCaseIdOrderByIdDesc(Long releaseId, Long testCaseId);

    Optional<Defect> findTopByOrderByIdDesc();

    long countByReleaseId(Long releaseId);
}
