package com.tuannv78.identity_service.service;

import com.tuannv78.entity.Role;
import com.tuannv78.entity.User;
import com.tuannv78.identity_service.common.dto.request.UserCreationRequest;
import com.tuannv78.identity_service.common.dto.request.UserUpdateRequest;
import com.tuannv78.identity_service.common.dto.response.UserResponse;
import com.tuannv78.identity_service.common.enums.ErrorCodeEnum;
import com.tuannv78.identity_service.common.enums.RoleEnum;
import com.tuannv78.identity_service.common.exception.AppException;
import com.tuannv78.identity_service.common.mapper.UserMapper;
import com.tuannv78.repository.RoleRepository;
import com.tuannv78.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    private final RoleRepository roleRepository;
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAll() {
        // Getting all permissions in database
        return userRepository.findAll()
                .stream()
                // transform for each row data into DTO list
                .map(userMapper::toUserResponse)
                .toList();
    }

    @PostAuthorize("returnObject.username == authentication.name || returnObject.username == 'admin'")
    public UserResponse getUserByID(String id) {
        // Step 1: Verify whether the user exists in the Database
        User user = userRepository.findById(id)
                // Throw exception when the user doesn't exist
                .orElseThrow(() -> new AppException(ErrorCodeEnum.USER_NOT_EXISTED));

        // Finally: return user response
        return userMapper.toUserResponse(user);
    }

    public UserResponse myInfo() {
        // Step 1: Get username form JWT token context
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Step 2: Verify whether the user exists in the Database
        User user = userRepository.findByUsername(username)
                // Throw exception when the user doesn't exist
                .orElseThrow(() -> new AppException(ErrorCodeEnum.USER_NOT_EXISTED));

        // Finally: return user response
        return userMapper.toUserResponse(user);
    }

    public UserResponse create(UserCreationRequest request) {
        // Step 1: Verify whether the user exists in the Database
        if (userRepository.existsByUsername(request.getUsername()))
            // Throw exception when the user already exists
            throw new AppException(ErrorCodeEnum.USER_EXISTED);

        // Step 2: Mapping DTO -> Entity
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Step 3: Add role default
        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder()
                .name(RoleEnum.USER.name())
                .description(RoleEnum.USER.getDescription())
                .build());
        user.setRoles(roles);

        // Finally: Return user response
        return userMapper.toUserResponse(
                // Save user into the database
                userRepository.save(user)
        );
    }

    @PostAuthorize("returnObject.username == authentication.name || returnObject.username == 'admin'")
    public UserResponse update(String userId, UserUpdateRequest request) {
        // Step 1: Verify whether the user exists in the Database
        User user = userRepository.findById(userId)
                // Throw exception when the user doesn't exist
                .orElseThrow(() -> new AppException(ErrorCodeEnum.USER_NOT_EXISTED));

        // Step 2: Check password
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // Step 3: Check first name
        if (request.getFirstName() != null && !request.getFirstName().isEmpty())
            user.setFirstName(request.getFirstName());

        // Step 4: Check last name
        if (request.getLastName() != null && !request.getLastName().isEmpty())
            user.setFirstName(request.getLastName());

        // Step 5: Check date of birth
        if (request.getDob() != null)
            user.setDob(request.getDob());

        // Step 6: Check roles
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            user.setRoles(new HashSet<>(roleRepository.findAllById(request.getRoles())));
        }

        // Finally: Return user response
        return userMapper.toUserResponse(
                // Save user into the database
                userRepository.save(user)
        );
    }

    @PostAuthorize("returnObject.username == authentication.name || returnObject.username == 'admin'")
    public void delete(String userId) {
        userRepository.deleteById(userId);
    }
}
