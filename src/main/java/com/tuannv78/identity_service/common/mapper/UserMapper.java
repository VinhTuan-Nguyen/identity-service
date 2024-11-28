package com.tuannv78.identity_service.common.mapper;

import com.tuannv78.identity_service.common.dto.request.UserCreationRequest;
import com.tuannv78.identity_service.common.dto.request.UserUpdateRequest;
import com.tuannv78.identity_service.common.dto.response.UserResponse;
import com.tuannv78.identity_service.common.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
