package com.example.find_your_partner.user.infrastructure;

import com.example.find_your_partner.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phone);
}
