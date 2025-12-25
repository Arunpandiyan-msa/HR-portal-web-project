package com.example.employee_management_system.repository;

import com.example.employee_management_system.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByEmpnameContainingIgnoreCaseOrEmpemailContainingIgnoreCaseOrEmpaddressContainingIgnoreCase(
            String name, String email, String address);
}




