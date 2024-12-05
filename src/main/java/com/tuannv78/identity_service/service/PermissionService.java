package com.tuannv78.identity_service.service;

import com.tuannv78.identity_service.common.dto.request.PermissionRequest;
import com.tuannv78.identity_service.common.dto.response.PermissionResponse;
import com.tuannv78.entity.Permission;
import com.tuannv78.identity_service.common.enums.PermissionEnum;
import com.tuannv78.identity_service.common.mapper.PermissionMapper;
import com.tuannv78.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toPermissionResponse).toList();
    }

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }
}
