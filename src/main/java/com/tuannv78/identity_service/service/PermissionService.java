package com.tuannv78.identity_service.service;

import com.tuannv78.entity.Permission;
import com.tuannv78.identity_service.common.dto.request.PermissionRequest;
import com.tuannv78.identity_service.common.dto.response.PermissionResponse;
import com.tuannv78.identity_service.common.enums.ErrorCodeEnum;
import com.tuannv78.identity_service.common.exception.AppException;
import com.tuannv78.identity_service.common.mapper.PermissionMapper;
import com.tuannv78.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public List<PermissionResponse> getAll() {
        // Getting all permissions in database
        return permissionRepository.findAll()
                .stream()
                // transform for each row data into DTO list
                .map(permissionMapper::toPermissionResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PermissionResponse create(PermissionRequest request) {
        // Step 1: Verify whether the permission exists in the Database
        if (permissionRepository.existsById(request.getName()))
            // Throw exception when the permission already exists
            throw new AppException(ErrorCodeEnum.PERMISSION_EXISTED);

        // Finnaly: Return permission response
        return permissionMapper.toPermissionResponse(
                // Save permission into the database
                permissionRepository.save(
                        // Mapping DTO -> Entity
                        permissionMapper.toPermission(request)
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PermissionResponse updateDescription(String request, String description) {
        // Step 1: Verify whether the permission exists in the Database
        Permission permission = permissionRepository.findById(request)
                // Throw exception when the permission does not exist
                .orElseThrow(() -> new AppException(ErrorCodeEnum.PERMISSION_NOT_EXISTED));

        // Step 2: Mapping new value
        if (description != null && !description.isBlank())
            permission.setDescription(description);

        // Finally: Return permission response
        return permissionMapper.toPermissionResponse(
                // Save permission into the database
                permissionRepository.save(permission)
        );

    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }
}
