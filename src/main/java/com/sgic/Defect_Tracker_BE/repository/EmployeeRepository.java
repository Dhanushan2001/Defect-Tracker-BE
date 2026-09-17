package com.sgic.Defect_Tracker_BE.repository;

import com.sgic.Defect_Tracker_BE.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByContactNo(String contactNo);

    boolean existsByContactNoAndIdNot(String contactNo, Long id);

    boolean existsByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

    boolean existsByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndIdNot(String firstName, String lastName, Long id);

    List<Employee> findByDesignationId(Long designationId);
}
