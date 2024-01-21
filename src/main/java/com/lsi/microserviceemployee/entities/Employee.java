package com.lsi.microserviceemployee.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
@TypeAlias("EMPLOYEE")
public class Employee extends User{
    @NotBlank
    @Size(max = 200)
    private String add_wallet_assu;
    @NotBlank
    @Size(max = 12)
    private String cin;
    @NotBlank
    private Date date_of_birth;

    public Employee(String id, String l_name, String f_name, String username, String password,
                    String email, String phone, String city, String address, String role,
                    String add_wallet_assu, String cin, Date date_of_birth) {

        super(id, l_name, f_name, username, password, email, phone, city, address, "EMPLOYEE");

        this.add_wallet_assu = add_wallet_assu;
        this.cin = cin;
        this.date_of_birth = date_of_birth;

    }


}
