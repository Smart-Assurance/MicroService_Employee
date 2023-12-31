package com.lsi.microserviceemployee.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeRequest {
    @NotBlank
    private String id;
    private String l_name;
    private String f_name;
    private String username;
    private String email;
    private String phone;
    private String city;
    private String address;
    private String cin;
    private Date date_of_birth;
}
