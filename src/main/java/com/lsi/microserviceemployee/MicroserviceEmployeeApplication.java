package com.lsi.microserviceemployee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class MicroserviceEmployeeApplication  {
    //implements CommandLineRunner
    /*@Autowired
    UserRepository userRepository ;*/
    public static void main(String[] args) {
        SpringApplication.run(MicroserviceEmployeeApplication.class, args);

    }
/*
    @Override
    public void run(String... args) throws Exception {
        Employee employee = new Employee (
                "123",
                "Doe",
                "John",
                "johndoe",
                "password123",
                "john@example.com",
                "123456789",
                "New York",
                "123 Street",
                "admin",
                "Some Wallet Assurance",
                "1234567890",
                new Date()
        );
        userRepository.save(employee);

    }*/
}
