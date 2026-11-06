package com.example.find_your_partner.user.application;

import com.example.find_your_partner.common.exception.ResourceNotFoundException;
import com.example.find_your_partner.user.api.UserResponse;
import com.example.find_your_partner.user.domain.User;
import com.example.find_your_partner.user.infrastructure.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(UUID userId){

        User user = userRepository.findById(userId)
                .orElseThrow(()->
                        new ResourceNotFoundException(
                                "USER_NOT_FOUND",
                                "User was not found."
                        )
                );

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getUserStatus(),
                user.getCreatedAt()
        );
    }
}
