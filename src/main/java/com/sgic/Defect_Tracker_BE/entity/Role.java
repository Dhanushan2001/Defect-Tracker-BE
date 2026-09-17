package com.sgic.Defect_Tracker_BE.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;

    @Column(name = "role_type")
    private String roleType;

    @Column(name = "description")
    private String description;

    public String getName() {
        return roleName;
    }
}
