package com.sgic.Defect_Tracker_BE.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "release_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReleaseType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "release_type_name", nullable = false, unique = true)
    private String releaseTypeName;

    @Column(name = "description")
    private String description;

    public String getName() {
        return releaseTypeName;
    }
}
