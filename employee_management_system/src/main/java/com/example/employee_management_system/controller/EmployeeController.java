package com.example.employee_management_system.controller;

import com.example.employee_management_system.model.Employee;
import com.example.employee_management_system.repository.AdminRepository;
import com.example.employee_management_system.repository.EmployeeRepository;
import com.example.employee_management_system.service.AdminService;
import com.example.employee_management_system.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/")
public class EmployeeController {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(EmployeeController.class);
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Autowired
    AdminService adminService;
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;



    @GetMapping("MSA")
    public String hr_page(Model model) {

        return "hr2_portal";
    }
    @GetMapping("HR portal site")
    public String hr_page_1(Model model) {

        return "hr_portal";
    }

    @GetMapping("employee records")
    public String getAllEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employee records";
    }


    @GetMapping("add")
    public String addEmployee(Model model) {
        model.addAttribute("addEmployee", new Employee());
        return "addEmployee";
    }
    @GetMapping("edit/{empid}")
    public String editEmployee(Model model, @PathVariable("empid") Long empid) {

        model.addAttribute("editEmployee", employeeService.getEmployeeById(empid));
        return "editEmployee";
    }
    @PostMapping("update/{empid}")
    public String updateEmployee( @PathVariable  Long empid,@ModelAttribute Employee employee, @RequestParam("profileImageFile") MultipartFile profileImageFile, RedirectAttributes redirectAttributes) {
        if (!profileImageFile.isEmpty()) {
            try {
                String fileName = StringUtils.cleanPath(profileImageFile.getOriginalFilename());
                employee.setProfileImage(fileName);

                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                try (InputStream inputStream = profileImageFile.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                    redirectAttributes.addFlashAttribute("successMessage", "Profile image updated successfully!");
                } catch (IOException e) {
                    logger.error("Could not save profile image: " + fileName, e);
                    redirectAttributes.addFlashAttribute("errorMessage", "Could not save profile image. Please try again.");
                }
            } catch (Exception e) {
                logger.error("An error occurred during profile image upload.", e);
                redirectAttributes.addFlashAttribute("errorMessage", "An error occurred during profile image upload. Please try again.");
            }
        }
        employeeService.save(employee);
        return "redirect:/employee records";
    }


    @PostMapping("save")
    public String saveEmployee(@ModelAttribute Employee employee, @RequestParam("profileImageFile") MultipartFile profileImageFile, RedirectAttributes redirectAttributes){
        if (!profileImageFile.isEmpty()) {
            try {
                String fileName = StringUtils.cleanPath(profileImageFile.getOriginalFilename());
                employee.setProfileImage(fileName);

                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                try (InputStream inputStream = profileImageFile.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                    redirectAttributes.addFlashAttribute("successMessage", "Profile image saved successfully!");
                } catch (IOException e) {
                    logger.error("Could not save profile image: " + fileName, e);
                    redirectAttributes.addFlashAttribute("errorMessage", "Could not save profile image. Please try again.");
                }
            } catch (Exception e) {
                logger.error("An error occurred during profile image upload.", e);
                redirectAttributes.addFlashAttribute("errorMessage", "An error occurred during profile image upload. Please try again.");
            }
        }
        employeeService.save(employee);
        return "redirect:/employee records";
    }

    //Search
    @GetMapping("employee/search")
    public String searchEmployees(@RequestParam("keyword") String keyword, Model model) {

        List<Employee> employees = employeeRepository
                .findByEmpnameContainingIgnoreCaseOrEmpemailContainingIgnoreCaseOrEmpaddressContainingIgnoreCase(
                        keyword, keyword, keyword
                );

        model.addAttribute("employees", employees);
        return "employee records";
    }

    //admin main page
    @GetMapping("admin_page")
    public String getAdminPage(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "admin_page";
    }


    //admin login
    @GetMapping("login")
    public String login(Model model) {
        return "admin_login";
    }

    @PostMapping("admin_login_page")
    public String employeeLogin(@RequestParam String username ,@RequestParam String password,  Model model) {
         boolean ok = adminService.ValidInput(username,password);
         if (ok) {
            return "/MSA";
         }
         return   "/HR portal site";
    }

    @GetMapping("/profile/{id}")
    public String userProfile(@PathVariable("id") long id, Model model) {
        System.out.println(id);
        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        return "user_profile";
    }
}

