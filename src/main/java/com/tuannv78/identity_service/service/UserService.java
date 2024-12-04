package com.tuannv78.identity_service.service;

import com.tuannv78.identity_service.common.dto.request.UserCreationRequest;
import com.tuannv78.identity_service.common.dto.request.UserUpdateRequest;
import com.tuannv78.identity_service.common.dto.response.UserResponse;
import com.tuannv78.identity_service.common.entity.Role;
import com.tuannv78.identity_service.common.entity.User;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    private final RoleRepository roleRepository;
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse create(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCodeEnum.USER_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var roles = new HashSet<Role>();
        roles.add(Role.builder()
                .name(RoleEnum.USER.name())
                .build());
        user.setRoles(roles);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCodeEnum.USER_EXISTED);
        }
        return userMapper.toUserResponse(user);
    }

    public UserResponse update(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        userMapper.updateUser(user, request);

        if (!user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        var roles = roleRepository.findAllById(request.getRoles());

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void delete(String userId) {
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @PostAuthorize("returnObject.username == authentication.name || returnObject.username == 'admin'")
    public UserResponse getUserByID(String id) {
        log.info("In method get User by ID");
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found"))
        );
    }

    public UserResponse myInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCodeEnum.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }
}
