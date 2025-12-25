package com.example.employee_management_system.service;

import com.example.employee_management_system.model.Admin;
import com.example.employee_management_system.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {
@Autowired
AdminRepository adminRepository;
    public boolean ValidInput(String username, String password) {
        Optional<Admin> admin = adminRepository.findByUsername(username);
          if (admin.isPresent()) {
              return admin.get().getPassword().equals(password);
          }
          else  {
              return false;
          }

    }
}
