package com.sgic.Defect_Tracker_BE.repository;

import com.sgic.Defect_Tracker_BE.entity.StatusWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusWorkflowRepository extends JpaRepository<StatusWorkflow, Long> {

    List<StatusWorkflow> findByFromStatusId(Long fromStatusId);

    List<StatusWorkflow> findByToStatusId(Long toStatusId);

    boolean existsByFromStatusIdAndToStatusId(Long fromStatusId, Long toStatusId);

    @Modifying
    @Query("DELETE FROM StatusWorkflow sw WHERE sw.fromStatus.id = :statusId OR sw.toStatus.id = :statusId")
    void deleteByStatusId(@Param("statusId") Long statusId);
}
