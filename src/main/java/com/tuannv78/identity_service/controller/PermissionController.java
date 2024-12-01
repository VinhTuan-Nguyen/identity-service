package com.tuannv78.identity_service.controller;

import com.tuannv78.identity_service.common.dto.response.ApiResponse;
import com.tuannv78.identity_service.common.dto.request.PermissionRequest;
import com.tuannv78.identity_service.common.dto.response.PermissionResponse;
import com.tuannv78.identity_service.common.enums.PermissionEnum;
import com.tuannv78.identity_service.model.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    @DeleteMapping("/{permission}")
    ApiResponse<Void> delete(@PathVariable PermissionEnum permission) {
        permissionService.delete(permission);
        return ApiResponse.<Void>builder()
                .message(String.format("Permission %s has deleted", permission))
                .build();
    }
}
