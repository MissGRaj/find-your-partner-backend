package com.example.find_your_partner.auth.application;

import com.example.find_your_partner.auth.api.RegisterRequest;
import com.example.find_your_partner.auth.api.RegisterResponse;
import com.example.find_your_partner.common.exception.ConflictException;
import com.example.find_your_partner.user.domain.User;
import com.example.find_your_partner.user.infrastructure.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request){

        String normalizedEmail = normalizeEmail(request.email());

        if(userRepository.existsByEmail(normalizedEmail)){
            throw new ConflictException(
                    "EMAIL_ALREADY_REGISTERED",
                    "An account already exists with this email address."
            );
        }

        String passwordHash = passwordEncoder.encode(request.password());

        User user = new User(
                normalizedEmail,
                passwordHash,
                request.phoneNumber()
        );

        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getPhoneNumber(),
                savedUser.getUserStatus(),
                savedUser.getCreatedAt()
        );
    }

    private String normalizeEmail(String email){
        return email.trim().toLowerCase(Locale.ROOT);
    }

}
