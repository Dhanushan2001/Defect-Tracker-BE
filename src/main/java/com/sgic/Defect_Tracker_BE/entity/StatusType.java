package com.sgic.Defect_Tracker_BE.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "status_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "position_x")
    private Double positionX;

    @Column(name = "position_y")
    private Double positionY;

    public String getStatusName() {
        return name;
    }

    public String getStatusType() {
        return type;
    }
}
