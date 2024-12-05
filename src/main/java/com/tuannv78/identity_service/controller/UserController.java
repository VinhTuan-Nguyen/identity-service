package com.tuannv78.identity_service.controller;

import com.tuannv78.identity_service.common.dto.request.UserCreationRequest;
import com.tuannv78.identity_service.common.dto.request.UserUpdateRequest;
import com.tuannv78.identity_service.common.dto.response.ApiResponse;
import com.tuannv78.identity_service.common.dto.response.UserResponse;
import com.tuannv78.identity_service.service.JsonService;
import com.tuannv78.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    JsonService json = new JsonService();
    UserService userService;

    @GetMapping
    ApiResponse<List<UserResponse>> getAll() throws IOException {
        // Logging request
        log.info("\nStarting get all user information");

        // Starting call user service
        ApiResponse<List<UserResponse>> response = ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAll())
                .build();

        // Logging response
        log.info("\nUsers information list received:\n{}", json.toString(response));

        return response;
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUserByID(@PathVariable String userId) throws IOException {
        // Logging request
        log.info("\nStarting get user information by user id.");

        // Starting call user service
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .result(userService.getUserByID(userId))
                .build();

        // Logging response
        log.info("\nUser information received:\n{}", json.toString(response));

        return response;
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> myInfo() throws IOException {
        // Logging request
        log.info("\nStarting get all current user information.");

        // Starting call user service
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .result(userService.myInfo())
                .build();

        // Logging response
        log.info("\nUser information received:\n{}", json.toString(response));

        return response;
    }

    @PostMapping
    ApiResponse<UserResponse> create(@RequestBody @Valid UserCreationRequest request) throws IOException {
        // Logging request
        log.info("\nStarting create user with:\n{}", request);

        // Starting call user service
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .message("User has been created")
                .result(userService.create(request))
                .build();

        // Logging response
        log.info("\nuser {} has been created:", response.getResult().getUsername());

        return response;
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> update(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        // Logging request
        log.info("\nStarting update user id: {}", userId);

        // Starting call user service
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .message("User has been updated")
                .result(userService.update(userId, request))
                .build();

        // Logging response
        log.info("\nuser {} has been updated:", response.getResult().getUsername());

        return response;
    }

    @DeleteMapping("/{userId}")
    ApiResponse<Void> delete(@PathVariable String userId) {
        // Logging request
        log.info("\nStarting deleted the user id: {}", userId);

        // Starting call user service
        userService.delete(userId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("User has been deleted")
                .build();

        // Logging response
        log.info("\nuser has been deleted:");

        return response;
    }
}
