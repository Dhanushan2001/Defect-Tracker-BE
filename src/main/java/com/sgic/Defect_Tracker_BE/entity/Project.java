package com.sgic.Defect_Tracker_BE.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
    name = "projects",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_project_name", columnNames = {"name"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "prefix")
    private String prefix;

    @Column(name = "project_type")
    private String projectType;

    @Column(name = "status", nullable = false)
    @Builder.Default
    private String status = "Active";

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_manager_id")
    private Employee projectManager;

    @Column(name = "manager_allocation")
    @Builder.Default
    private Integer managerAllocation = 100;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "client_country")
    private String clientCountry;

    @Column(name = "client_state")
    private String clientState;

    @Column(name = "client_email")
    private String clientEmail;

    @Column(name = "client_phone")
    private String clientPhone;

    @Column(name = "address", length = 1000)
    private String address;
}
