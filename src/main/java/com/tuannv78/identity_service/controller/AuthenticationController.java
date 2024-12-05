package com.tuannv78.identity_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.JOSEException;
import com.tuannv78.identity_service.common.dto.request.AuthenticationRequest;
import com.tuannv78.identity_service.common.dto.request.IntrospectRequest;
import com.tuannv78.identity_service.common.dto.request.LogoutRequest;
import com.tuannv78.identity_service.common.dto.request.RefreshRequest;
import com.tuannv78.identity_service.common.dto.response.ApiResponse;
import com.tuannv78.identity_service.common.dto.response.AuthenticationResponse;
import com.tuannv78.identity_service.common.dto.response.IntrospectResponse;
import com.tuannv78.identity_service.service.AuthenticationService;
import com.tuannv78.identity_service.service.JsonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    JsonService object = new JsonService();
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request)
            throws IOException {
        try {
            // Logging request
            log.info("\nStarting request JWT token with:\n{}", object.toString(request));

            // Starting call authenticate service
            ApiResponse<AuthenticationResponse> response = ApiResponse.<AuthenticationResponse>builder()
                    .result(authenticationService.authenticate(request))
                    .build();

            // Logging response
            log.info("\nSuccessfully request JWT token:\n{}", object.toString(response));

            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException, IOException {
        try {
            // Logging request
            log.info("\nStarting verify JWT token with:\n{}", object.toString(request));

            // Starting call authenticate service
            ApiResponse<IntrospectResponse> response = ApiResponse.<IntrospectResponse>builder()
                    .result(authenticationService.introspect(request))
                    .build();

            // Logging response
            log.info("\nSuccessfully verify JWT token:\n{}", object.toString(response));

            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException, IOException {
        try {
            // Logging request
            log.info("\nStarting refresh JWT token with:\n{}", object.toString(request));

            // Starting call authenticate service
            ApiResponse<AuthenticationResponse> response = ApiResponse.<AuthenticationResponse>builder()
                    .result(authenticationService.refreshToken(request))
                    .build();

            // Logging response
            log.info("\nSuccessfully refresh JWT token:\n{}", object.toString(response));

            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException, IOException {
        try {
            // Logging request
            log.info("\nStarting Logout JWT token with:\n{}", object.toString(request));

            // Starting call authenticate service
            authenticationService.logout(request);

            // Logging response
            log.info("\nSuccessfully logout JWT token");

            return ApiResponse.<Void>builder()
                    .message("User has been logout")
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
