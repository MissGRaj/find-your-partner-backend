package com.example.find_your_partner.auth.application;

import com.example.find_your_partner.auth.api.LoginRequest;
import com.example.find_your_partner.auth.api.LoginResponse;
import com.example.find_your_partner.auth.jwt.JwtProperties;
import com.example.find_your_partner.auth.jwt.JwtService;
import com.example.find_your_partner.common.exception.InvalidCredentialsException;
import com.example.find_your_partner.user.domain.User;
import com.example.find_your_partner.user.domain.UserStatus;
import com.example.find_your_partner.user.infrastructure.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    public LoginService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            JwtProperties jwtProperties
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
    }

    public LoginResponse login(LoginRequest request){

        String email = request.email()
                .trim()
                .toLowerCase(Locale.ROOT);

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if(!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash()
        )){
            throw new InvalidCredentialsException();
        }

        if (user.getUserStatus() != UserStatus.ACTIVE){
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateAccessToken(user.getId(), user.getEmail());

        return new LoginResponse(
                token,
                "Bearer",
                jwtProperties.accessTokenExpiration().toSeconds()
        );
    }

}
