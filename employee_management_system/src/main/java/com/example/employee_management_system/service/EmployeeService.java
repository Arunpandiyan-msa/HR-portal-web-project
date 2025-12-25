package com.example.employee_management_system.service;
import com.example.employee_management_system.model.Employee;
import com.example.employee_management_system.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
     @Autowired
     private EmployeeRepository employeeRepository;

     public  EmployeeService(EmployeeRepository employeeRepository )
     {
         this.employeeRepository = employeeRepository;
     }

    public List <Employee> getAllEmployees() {
         return employeeRepository.findAll();
    }


     public void save(Employee employee){
         employeeRepository.save(employee);
     }

    public Employee getEmployeeById(long id) {
        return employeeRepository.findById(id).orElse(null);
    }



}
