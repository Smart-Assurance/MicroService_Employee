package com.lsi.microserviceemployee.controllers;

import com.lsi.microserviceemployee.entities.Employee;
import com.lsi.microserviceemployee.payload.request.AddEmployeeRequest;
import com.lsi.microserviceemployee.payload.request.UpdateEmployeeRequest;
import com.lsi.microserviceemployee.payload.response.MessageResponse;
import com.lsi.microserviceemployee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    public EmployeeRepository employeeRepository;

    @PostMapping("/add")
    public ResponseEntity<MessageResponse> addEmployee(@RequestBody AddEmployeeRequest addEmployeeRequest) {
        try {
            Employee employee = new Employee(
                    null,
                    addEmployeeRequest.getL_name(),
                    addEmployeeRequest.getF_name(),
                    addEmployeeRequest.getUsername(),
                    encoder.encode(addEmployeeRequest.getDate_of_birth().toString()),
                    addEmployeeRequest.getEmail(),
                    addEmployeeRequest.getPhone(),
                    addEmployeeRequest.getCity(),
                    addEmployeeRequest.getAddress(),
                    "EMPLOYEE",
                    "",
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
    public ResponseEntity<List<Employee>> getAllEmployees() {
        try {
            List<Employee> employees = employeeRepository.findAll();
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(500).build(); // Erreur interne du serveur
        }
    }


    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String employeeId) {
        try {
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
            @RequestBody UpdateEmployeeRequest updatedEmployeeRequest
    ) {
        try {
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
    public ResponseEntity<MessageResponse> deleteEmployee(@PathVariable String employeeId) {
        try {
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
