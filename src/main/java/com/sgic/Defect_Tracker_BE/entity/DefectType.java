package com.sgic.Defect_Tracker_BE.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "defect_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DefectType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "defect_type_name", nullable = false, unique = true)
    private String defectTypeName;

    @Column(name = "description")
    private String description;

    public String getName() {
        return defectTypeName;
    }
}
