package com.tuannv78.identity_service.common.configuration;

import com.tuannv78.identity_service.common.entity.Role;
import com.tuannv78.identity_service.common.entity.User;
import com.tuannv78.identity_service.common.enums.RoleEnum;
import com.tuannv78.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {

    static String INIT_USERNAME = "admin";
    static String INIT_PASSWORD = "admin";
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername(INIT_USERNAME).isEmpty()) {
                var roles = new HashSet<Role>();
                roles.add(Role.builder().name(RoleEnum.ADMIN.name()).build());

                User user = User.builder()
                        .username(INIT_USERNAME)
                        .password(passwordEncoder.encode(INIT_PASSWORD))
                        .roles(roles)
                        .build();

                userRepository.save(user);

                log.warn("\nAdmin user has been created by default with the following information:\nUsername: {}\nPassword: {}",
                        INIT_USERNAME,
                        INIT_PASSWORD
                );
            }
        };
    }
}
