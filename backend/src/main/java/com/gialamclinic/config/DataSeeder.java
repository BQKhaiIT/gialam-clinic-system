package com.gialamclinic.config;

import com.gialamclinic.entity.User;
import com.gialamclinic.enums.Role;
import com.gialamclinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args)  {
        if(userRepository.findByUsername("admin").isEmpty()) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("123456"));
            user.setFullName("System Admin");
            user.setRole(Role.ADMIN);
            user.setIsActive(true);
            userRepository.save(user);
        }
    }
}
