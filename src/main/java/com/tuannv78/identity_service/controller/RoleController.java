package com.tuannv78.identity_service.controller;

import com.tuannv78.identity_service.dto.ApiResponse;
import com.tuannv78.identity_service.dto.request.PermissionRequest;
import com.tuannv78.identity_service.dto.request.RoleRequest;
import com.tuannv78.identity_service.dto.response.PermissionResponse;
import com.tuannv78.identity_service.dto.response.RoleResponse;
import com.tuannv78.identity_service.entity.Role;
import com.tuannv78.identity_service.enums.RoleEnum;
import com.tuannv78.identity_service.model.service.PermissionService;
import com.tuannv78.identity_service.model.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{role}")
    ApiResponse<Void> delete(@PathVariable RoleEnum role) {
        roleService.delete(role);
        return ApiResponse.<Void>builder()
                .message(String.format("Role %s has deleted", role))
                .build();
    }
}
