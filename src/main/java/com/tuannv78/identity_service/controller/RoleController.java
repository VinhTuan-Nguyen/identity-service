package com.tuannv78.identity_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuannv78.identity_service.common.dto.request.RoleRequest;
import com.tuannv78.identity_service.common.dto.response.ApiResponse;
import com.tuannv78.identity_service.common.dto.response.RoleResponse;
import com.tuannv78.identity_service.service.JsonService;
import com.tuannv78.identity_service.service.RoleService;
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
    JsonService object = new JsonService();
    RoleService roleService;

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
        try {
            // Logging request
            log.info("\nStarting get all role.");

            // Starting call permission service
            ApiResponse<List<RoleResponse>> response = ApiResponse.<List<RoleResponse>>builder().result(roleService.getAll()).build();

            // Logging response
            log.info("\nRole list:\n{}", object.toString(response));

            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        try {
            // Logging request
            log.info("\nStarting create role with: {}", object.toString(request));

            // Starting call permission service
            ApiResponse<RoleResponse> response = ApiResponse.<RoleResponse>builder().result(roleService.create(request)).build();

            // Logging response
            log.info("\nNew role created:\n{}", object.toString(response));

            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{role}")
    ApiResponse<Void> delete(@PathVariable String role) {
        try {
            // Logging request
            log.info("\nStarting delete permission: {}", object.toString(role));

            // Starting call permission service
            roleService.delete(role);
            ApiResponse<Void> response = ApiResponse.<Void>builder().message(String.format("Role %s has been deleted", role)).build();

            // Logging response
            log.info("\nPermission deleted:\n{}", object.toString(response));

            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
