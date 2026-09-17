package com.sgic.Defect_Tracker_BE.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "status_workflows",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_status_workflow_transition", columnNames = {"from_status_id", "to_status_id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusWorkflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "from_status_id", nullable = false)
    private StatusType fromStatus;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "to_status_id", nullable = false)
    private StatusType toStatus;
}
