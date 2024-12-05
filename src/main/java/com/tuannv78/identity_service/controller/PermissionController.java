package com.tuannv78.identity_service.controller;

import com.tuannv78.identity_service.common.dto.request.PermissionRequest;
import com.tuannv78.identity_service.common.dto.response.ApiResponse;
import com.tuannv78.identity_service.common.dto.response.PermissionResponse;
import com.tuannv78.identity_service.service.JsonService;
import com.tuannv78.identity_service.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    JsonService json = new JsonService();
    PermissionService permissionService;

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll()
            throws IOException {
        // Logging request
        log.info("\nStarting get all permissions.");

        // Starting call permission service
        ApiResponse<List<PermissionResponse>> response = ApiResponse.<List<PermissionResponse>>builder().result(permissionService.getAll()).build();

        // Logging response
        log.info("\nPermissions list:\n{}", json.toString(response));

        return response;
    }

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request)
            throws IOException {
        // Logging request
        log.info("\nStarting create permissions with: {}", json.toString(request));

        // Starting call permission service
        ApiResponse<PermissionResponse> response = ApiResponse.<PermissionResponse>builder().result(permissionService.create(request)).build();

        // Logging response
        log.info("\nNew permission created:\n{}", json.toString(response));

        return response;
    }

    @DeleteMapping("/{permission}")
    ApiResponse<Void> delete(@PathVariable String permission)
            throws IOException {
        // Logging request
        log.info("\nStarting delete permission: {}", json.toString(permission));

        // Starting call permission service
        permissionService.delete(permission);
        ApiResponse<Void> response = ApiResponse.<Void>builder().message(String.format("Permission %s has been deleted", permission)).build();

        // Logging response
        log.info("\nPermission deleted:\n{}", json.toString(response));

        return response;
    }
}
