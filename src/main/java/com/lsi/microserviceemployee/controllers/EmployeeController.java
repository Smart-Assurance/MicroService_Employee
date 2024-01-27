package com.lsi.microserviceemployee.controllers;

import com.lsi.microserviceemployee.entities.Employee;
import com.lsi.microserviceemployee.payload.request.AddEmployeeRequest;
import com.lsi.microserviceemployee.payload.request.UpdateEmployeeRequest;
import com.lsi.microserviceemployee.payload.response.MessageResponse;
import com.lsi.microserviceemployee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    public EmployeeRepository employeeRepository;

    private final AuthService authService;

    public EmployeeController(EmployeeRepository employeeRepository, AuthService authService) {
        this.employeeRepository = employeeRepository;
        this.authService = authService;
    }

    public String encodeDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return encoder.encode(sdf.format(date));
    }

    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }


    @PostMapping("/add")
    public ResponseEntity<MessageResponse> addEmployee(@RequestBody AddEmployeeRequest addEmployeeRequest,@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extract the token from the Authorization header
            String token = extractTokenFromHeader(authorizationHeader);
            if (!authService.isValidAdministratorToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(401, "Not authorized"));
            }
            Employee employee = new Employee(
                    null,
                    addEmployeeRequest.getL_name(),
                    addEmployeeRequest.getF_name(),
                    addEmployeeRequest.getUsername(),
                    encodeDate(addEmployeeRequest.getDate_of_birth()),
                    addEmployeeRequest.getEmail(),
                    addEmployeeRequest.getPhone(),
                    addEmployeeRequest.getCity(),
                    addEmployeeRequest.getAddress(),
                    "EMPLOYEE",
                    addEmployeeRequest.getAdd_wallet_assu(),
                    addEmployeeRequest.getCin(),
                    addEmployeeRequest.getDate_of_birth()
            );

            employeeRepository.save(employee);

            return ResponseEntity.ok(new MessageResponse(201,"Employee saved successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(400,"Employee doesn't save "));

        }
    }
    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllEmployees(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extract the token from the Authorization header
            String token = extractTokenFromHeader(authorizationHeader);
            if (!authService.isValidAdministratorToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(401, "Not authorized"));
            }
            
            List<Employee> employees = employeeRepository.findAll();
            List<Employee> filteredEmployees = employees.stream()
                    .filter(employee -> hasRole(employee, "ROLE_EMPLOYEE"))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(filteredEmployees);
        } catch (Exception e) {
            return ResponseEntity.status(500).build(); // Erreur interne du serveur
        }
    }

    private boolean hasRole(Employee employee, String role) {
        return employee.getRole().equals(role);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<Object> getEmployeeById(@PathVariable String employeeId,@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extract the token from the Authorization header
            String token = extractTokenFromHeader(authorizationHeader);
            if (!authService.isValidAdministratorToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(401, "Not authorized"));
            }
            Optional<Employee> employee = employeeRepository.findById(employeeId);
            if (employee.isPresent()) {
                return ResponseEntity.ok(employee.get());
            } else {
                return ResponseEntity.status(404).build(); // Ressource non trouvée
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build(); // Erreur interne du serveur
        }
    }



    @PutMapping("/{employeeId}")
    public ResponseEntity<MessageResponse> updateEmployee(
            @PathVariable String employeeId,
            @RequestBody UpdateEmployeeRequest updatedEmployeeRequest,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            // Extract the token from the Authorization header
            String token = extractTokenFromHeader(authorizationHeader);
            if (!authService.isValidAdministratorToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(401, "Not authorized"));
            }
            Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
            if (optionalEmployee.isPresent()) {
                Employee employee = optionalEmployee.get();

                // Mettre à jour tous les champs de l'employé
                employee.setL_name(updatedEmployeeRequest.getL_name());
                employee.setF_name(updatedEmployeeRequest.getF_name());
                employee.setUsername(updatedEmployeeRequest.getUsername());
                employee.setEmail(updatedEmployeeRequest.getEmail());
                employee.setPhone(updatedEmployeeRequest.getPhone());
                employee.setCity(updatedEmployeeRequest.getCity());
                employee.setAddress(updatedEmployeeRequest.getAddress());
                employee.setCin(updatedEmployeeRequest.getCin());
                employee.setDate_of_birth(updatedEmployeeRequest.getDate_of_birth());

                employeeRepository.save(employee);
                return ResponseEntity.ok(new MessageResponse(200, "Employee updated successfully"));
            } else {
                return ResponseEntity.status(404).body(new MessageResponse(404, "Employee not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse(500, "Internal server error"));
        }
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<MessageResponse> deleteEmployee(@PathVariable String employeeId,@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extract the token from the Authorization header
            String token = extractTokenFromHeader(authorizationHeader);
            if (!authService.isValidAdministratorToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(401, "Not authorized"));
            }
            Optional<Employee> employee = employeeRepository.findById(employeeId);
            if (employee.isPresent()) {
                employeeRepository.delete(employee.get());
                return ResponseEntity.ok(new MessageResponse(200, "Employee deleted successfully"));
            } else {
                return ResponseEntity.status(404).body(new MessageResponse(404, "Employee not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse(500, "Internal server error"));
        }
    }
}
