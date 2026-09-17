package com.sgic.Defect_Tracker_BE.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "severities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Severity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "weight", nullable = false, unique = true)
    private Integer weight;

    @Column(name = "description")
    private String description;
}
