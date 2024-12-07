package com.tuannv78.identity_service.service;

import com.tuannv78.entity.Permission;
import com.tuannv78.entity.Role;
import com.tuannv78.identity_service.common.dto.request.RoleRequest;
import com.tuannv78.identity_service.common.dto.response.RoleResponse;
import com.tuannv78.identity_service.common.enums.ErrorCodeEnum;
import com.tuannv78.identity_service.common.exception.AppException;
import com.tuannv78.identity_service.common.mapper.RoleMapper;
import com.tuannv78.repository.PermissionRepository;
import com.tuannv78.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    PermissionRepository permissionRepository;
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    public List<RoleResponse> getAll() {
        // Getting all roles in database
        return roleRepository.findAll()
                .stream()
                // transform for each row data into DTO list
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public RoleResponse create(RoleRequest request) {
        // Step 1: Verify whether the role exists in the Database
        if (roleRepository.existsById(request.getName()))
            // Throw exception when the role already exists
            throw new AppException(ErrorCodeEnum.ROLE_EXISTED);

        // Step 2: Mapping DTO -> Entity
        Role role = roleMapper.toRole(request);
        List<Permission> permission = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permission));

        // Finally: return role response
        return roleMapper.toRoleResponse(
                // Save role into the database
                roleRepository.save(role)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    public RoleResponse update(RoleRequest request) {
        // Step 1: Verify whether the role exists in the Database
        Role role = roleRepository.findById(request.getName())
                // Throw exception when the role does not exist
                .orElseThrow(() -> new AppException(ErrorCodeEnum.ROLE_NOT_EXISTED));

        // Step 2: Check description
        if (request.getDescription() != null && !request.getDescription().isBlank())
            role.setDescription(request.getDescription());

        // Step 3: Check permissions
        if (request.getPermissions() != null && !request.getPermissions().isEmpty())
            role.setPermissions(new HashSet<>(permissionRepository.findAllById(request.getPermissions())));

        // Finally: Return role response
        return roleMapper.toRoleResponse(
                // Save role into the database
                roleRepository.save(role)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String role) {
        roleRepository.deleteById(role);
    }
}
