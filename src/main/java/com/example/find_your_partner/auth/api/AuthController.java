package com.example.find_your_partner.auth.api;

import com.example.find_your_partner.auth.application.LoginService;
import com.example.find_your_partner.auth.application.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private RegistrationService registrationService;
    private LoginService loginService;

    public AuthController(
            RegistrationService registrationService,
            LoginService loginService
    ){
        this.loginService = loginService;
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request
    ){
        RegisterResponse response = registrationService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ){
        return ResponseEntity.ok(loginService.login(request));
    }

}
