package com.lsi.microserviceemployee.repository;

import com.lsi.microserviceemployee.entities.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {

}
