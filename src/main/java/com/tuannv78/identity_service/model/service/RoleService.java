package com.tuannv78.identity_service.model.service;

import com.tuannv78.identity_service.common.dto.request.RoleRequest;
import com.tuannv78.identity_service.common.dto.response.RoleResponse;
import com.tuannv78.identity_service.common.enums.RoleEnum;
import com.tuannv78.identity_service.common.mapper.RoleMapper;
import com.tuannv78.identity_service.model.repository.PermissionRepository;
import com.tuannv78.identity_service.model.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    private final PermissionRepository permissionRepository;
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);
        var permission = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permission));
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public void delete(RoleEnum role) {
        roleRepository.deleteById(role);
    }
}
