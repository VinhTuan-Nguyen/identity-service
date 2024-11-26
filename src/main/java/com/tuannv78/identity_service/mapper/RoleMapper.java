package com.tuannv78.identity_service.mapper;

import com.tuannv78.identity_service.dto.request.PermissionRequest;
import com.tuannv78.identity_service.dto.request.RoleRequest;
import com.tuannv78.identity_service.dto.response.PermissionResponse;
import com.tuannv78.identity_service.dto.response.RoleResponse;
import com.tuannv78.identity_service.entity.Permission;
import com.tuannv78.identity_service.entity.Role;
import com.tuannv78.identity_service.enums.RoleEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
