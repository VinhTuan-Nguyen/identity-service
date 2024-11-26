package com.tuannv78.identity_service.model.service;

import com.tuannv78.identity_service.dto.request.PermissionRequest;
import com.tuannv78.identity_service.dto.response.PermissionResponse;
import com.tuannv78.identity_service.entity.Permission;
import com.tuannv78.identity_service.enums.PermissionEnum;
import com.tuannv78.identity_service.mapper.PermissionMapper;
import com.tuannv78.identity_service.model.repository.PermissionRepository;
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

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(PermissionEnum permission) {
        permissionRepository.deleteById(permission);
    }
}
