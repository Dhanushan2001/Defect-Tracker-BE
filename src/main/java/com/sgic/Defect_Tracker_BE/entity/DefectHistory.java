package com.sgic.Defect_Tracker_BE.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "defect_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DefectHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "defect_id", nullable = false)
    private Defect defect;

    @Column(name = "assigned_by_name")
    private String assignedByName;

    @Column(name = "assigned_to_name")
    private String assignedToName;

    @Column(name = "previous_status")
    private String previousStatus;

    @Column(name = "defect_status")
    private String defectStatus;

    @Column(name = "name", length = 1000)
    private String name;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
