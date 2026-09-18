package com.sgic.Defect_Tracker_BE.repository;

import com.sgic.Defect_Tracker_BE.entity.DefectComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefectCommentRepository extends JpaRepository<DefectComment, Long> {

    List<DefectComment> findByDefectIdOrderByIdAsc(Long defectId);

    long countByDefectId(Long defectId);

    @Modifying
    @Query("DELETE FROM DefectComment dc WHERE dc.defect.id = :defectId")
    void deleteByDefectId(@Param("defectId") Long defectId);
}
