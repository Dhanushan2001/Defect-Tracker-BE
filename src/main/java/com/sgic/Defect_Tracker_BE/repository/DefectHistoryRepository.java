package com.sgic.Defect_Tracker_BE.repository;

import com.sgic.Defect_Tracker_BE.entity.DefectHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefectHistoryRepository extends JpaRepository<DefectHistory, Long> {

    List<DefectHistory> findByDefectIdOrderByIdDesc(Long defectId);

    @Modifying
    @Query("DELETE FROM DefectHistory dh WHERE dh.defect.id = :defectId")
    void deleteByDefectId(@Param("defectId") Long defectId);
}
