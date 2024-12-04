package com.tuannv78.identity_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    JsonService object = new JsonService();
    PermissionService permissionService;

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll() {
        try {
            // Logging request
            log.info("\nStarting get all permissions.");

            // Starting call permission service
            ApiResponse<List<PermissionResponse>> response = ApiResponse.<List<PermissionResponse>>builder().result(permissionService.getAll()).build();

            // Logging response
            log.info("\nPermissions list:\n{}", object.toString(response));

            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        try {
            // Logging request
            log.info("\nStarting create permissions with: {}", object.toString(request));

            // Starting call permission service
            ApiResponse<PermissionResponse> response = ApiResponse.<PermissionResponse>builder().result(permissionService.create(request)).build();

            // Logging response
            log.info("\nNew permission created:\n{}", object.toString(response));

            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{permission}")
    ApiResponse<Void> delete(@PathVariable String permission) {
        try {
            // Logging request
            log.info("\nStarting delete permission: {}", object.toString(permission));

            // Starting call permission service
            permissionService.delete(permission);
            ApiResponse<Void> response = ApiResponse.<Void>builder().message(String.format("Permission %s has been deleted", permission)).build();

            // Logging response
            log.info("\nPermission deleted:\n{}", object.toString(response));

            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
