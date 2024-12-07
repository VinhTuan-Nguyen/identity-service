package com.tuannv78.identity_service.common.configuration;

import com.tuannv78.entity.Permission;
import com.tuannv78.entity.Role;
import com.tuannv78.entity.User;
import com.tuannv78.identity_service.common.enums.PermissionEnum;
import com.tuannv78.identity_service.common.enums.RoleEnum;
import com.tuannv78.identity_service.service.JsonService;
import com.tuannv78.repository.PermissionRepository;
import com.tuannv78.repository.RoleRepository;
import com.tuannv78.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;
    JsonService json = new JsonService();

    @NonFinal
    @Value("${spring.application.init-user.username}")
    String INIT_USERNAME;

    @NonFinal
    @Value("${spring.application.init-user.password}")
    String INIT_PASSWORD;

    @Bean
    ApplicationRunner applicationRunner(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository
    ) {
        return args -> {
            // If the admin user does not exist. init a default admin user
            log.info("\nStarting check user default");
            if (userRepository.findByUsername(INIT_USERNAME).isEmpty()) createAdminUser(userRepository);
            else log.info("\nChecking user default completed");

            // If The role does not exist. init a role default
            log.info("\nStarting check role default");
            for (RoleEnum r : RoleEnum.values()) {
                if (roleRepository.findById(r.name()).isEmpty()) {
                    Role role = Role.builder()
                            .name(r.name())
                            .description(r.getDescription())
                            .build();
                    roleRepository.save(role);
                    log.info("\nNew role created with: {}", json.toString(role));
                }
            }
            log.info("\nChecking role default completed");

            // If the permission does not exist. init permissions default
            log.info("\nStarting check permissions default");
            for (PermissionEnum per : PermissionEnum.values()) {
                if (permissionRepository.findById(per.name()).isEmpty()) {
                    Permission permission = Permission.builder()
                            .name(per.name())
                            .description(per.getDescription())
                            .build();
                    permissionRepository.save(permission);
                    log.info("\nNew permission created with: {}", json.toString(permission));
                }
            }
            log.info("\nChecking permission default completed");
        };
    }

    private void createAdminUser(UserRepository userRepository) {
        // Define user roles
        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder()
                .name(RoleEnum.ADMIN.name())
                .description(RoleEnum.ADMIN.getDescription())
                .build()
        );

        // Create new user
        User user = User.builder()
                .username(INIT_USERNAME)
                .password(passwordEncoder.encode(INIT_PASSWORD))
                .roles(roles)
                .build();

        // Executing SQL to store the user
        userRepository.save(user);

        // Logging info
        log.warn("\nAdmin user has been created by default with:\nUsername: {}\nPassword: {}",
                INIT_USERNAME,
                INIT_PASSWORD
        );
    }
}
