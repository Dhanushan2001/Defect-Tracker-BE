package com.sgic.Defect_Tracker_BE.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "designations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Designation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "designation_name", nullable = false, unique = true)
    private String designationName;

    @Column(name = "description")
    private String description;

    public String getName() {
        return designationName;
    }
}
