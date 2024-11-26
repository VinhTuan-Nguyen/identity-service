package com.tuannv78.identity_service.mapper;

import com.tuannv78.identity_service.dto.request.PermissionRequest;
import com.tuannv78.identity_service.dto.request.UserCreationRequest;
import com.tuannv78.identity_service.dto.request.UserUpdateRequest;
import com.tuannv78.identity_service.dto.response.PermissionResponse;
import com.tuannv78.identity_service.dto.response.UserResponse;
import com.tuannv78.identity_service.entity.Permission;
import com.tuannv78.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}