package com.sgic.Defect_Tracker_BE.repository;

import com.sgic.Defect_Tracker_BE.entity.Severity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeverityRepository extends JpaRepository<Severity, Long> {
    Optional<Severity> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
    boolean existsByWeight(Integer weight);
    boolean existsByWeightAndIdNot(Integer weight, Long id);
}

