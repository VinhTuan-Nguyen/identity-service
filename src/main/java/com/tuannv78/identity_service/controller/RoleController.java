package com.tuannv78.identity_service.controller;

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

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    JsonService json = new JsonService();
    RoleService roleService;

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() throws IOException {
        // Logging request
        log.info("\nStarting get all role.");

        // Starting call permission service
        ApiResponse<List<RoleResponse>> response = ApiResponse.<List<RoleResponse>>builder().result(roleService.getAll()).build();

        // Logging response
        log.info("\nRole list:\n{}", json.toString(response));

        return response;
    }

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) throws IOException {
        // Logging request
        log.info("\nStarting create role with: {}", json.toString(request));

        // Starting call permission service
        ApiResponse<RoleResponse> response = ApiResponse.<RoleResponse>builder().result(roleService.create(request)).build();

        // Logging response
        log.info("\nNew role created:\n{}", json.toString(response));

        return response;
    }

    @PutMapping
    ApiResponse<RoleResponse> update(@RequestBody RoleRequest role)
            throws IOException {
        // Logging request
        log.info("\nStarting update role with:\n{}'", role);

        // Starting call role service
        ApiResponse<RoleResponse> response = ApiResponse.<RoleResponse>builder()
                .result(roleService.update(role))
                .build();

        // Logging response
        log.info("\ndescription updated:\n{}", json.toString(response));

        return response;
    }

    @DeleteMapping("/{role}")
    ApiResponse<Void> delete(@PathVariable String role) throws IOException {
        // Logging request
        log.info("\nStarting delete permission: {}", json.toString(role));

        // Starting call permission service
        roleService.delete(role);
        ApiResponse<Void> response = ApiResponse.<Void>builder().message(String.format("Role %s has been deleted", role)).build();

        // Logging response
        log.info("\nPermission deleted:\n{}", json.toString(response));

        return response;
    }
}
