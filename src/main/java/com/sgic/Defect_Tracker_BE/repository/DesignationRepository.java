package com.sgic.Defect_Tracker_BE.repository;

import com.sgic.Defect_Tracker_BE.entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Long> {
    Optional<Designation> findByDesignationNameIgnoreCase(String designationName);
    boolean existsByDesignationNameIgnoreCase(String designationName);
    boolean existsByDesignationNameIgnoreCaseAndIdNot(String designationName, Long id);
}
