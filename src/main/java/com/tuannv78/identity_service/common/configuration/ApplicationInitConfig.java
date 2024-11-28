package com.tuannv78.identity_service.common.configuration;

import com.tuannv78.identity_service.common.entity.Role;
import com.tuannv78.identity_service.common.entity.User;
import com.tuannv78.identity_service.common.enums.RoleEnum;
import com.tuannv78.identity_service.model.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                var roles = new HashSet<Role>();
                roles.add(Role.builder().name(RoleEnum.ADMIN.name()).build());

                new User();
                User user = User.builder().username("admin").password(passwordEncoder.encode("admin")).roles(roles).build();
                userRepository.save(user);
                log.warn("\n\nAdmin user has been created by default with the following information:\nUsername: admin\nPassword: admin");
            }
        };
    }
}
