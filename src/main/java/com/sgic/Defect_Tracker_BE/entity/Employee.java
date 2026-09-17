package com.sgic.Defect_Tracker_BE.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
    name = "employees",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_employee_email", columnNames = {"email"}),
        @UniqueConstraint(name = "uk_employee_contact_no", columnNames = {"contact_no"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "contact_no", nullable = false, unique = true)
    private String contactNo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "designation_id")
    private Designation designation;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "skills", length = 1000)
    private String skills;

    @Column(name = "experience")
    @Builder.Default
    private Integer experience = 0;

    @Column(name = "availability")
    @Builder.Default
    private Integer availability = 100;
}
