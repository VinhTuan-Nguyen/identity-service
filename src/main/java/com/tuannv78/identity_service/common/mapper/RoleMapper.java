package com.tuannv78.identity_service.common.mapper;

import com.tuannv78.identity_service.common.dto.request.RoleRequest;
import com.tuannv78.identity_service.common.dto.response.RoleResponse;
import com.tuannv78.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
