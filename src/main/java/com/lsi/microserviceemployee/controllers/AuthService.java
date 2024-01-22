package com.lsi.microserviceemployee.controllers;


import com.lsi.microserviceemployee.payload.request.TokenValidationRequest;
import com.lsi.microserviceemployee.payload.response.TokenValidationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    @Value("${auth.service.url}")
    private String authServiceUrl;

    private final RestTemplate restTemplate;

    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isValidAdministratorToken(String token) {
        String validationUrl = authServiceUrl + "/validate-token";
        TokenValidationRequest validationRequest = new TokenValidationRequest();
        validationRequest.setToken(token);
        ResponseEntity<TokenValidationResponse> responseEntity = restTemplate.postForEntity(validationUrl, validationRequest, TokenValidationResponse.class);
        return responseEntity.getBody().isResponse() == true && "administrator".equals(responseEntity.getBody().getMessage());
    }
}