package com.tuannv78.identity_service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tuannv78.entity.Token;
import com.tuannv78.entity.User;
import com.tuannv78.identity_service.common.dto.request.AuthenticationRequest;
import com.tuannv78.identity_service.common.dto.request.IntrospectRequest;
import com.tuannv78.identity_service.common.dto.request.LogoutRequest;
import com.tuannv78.identity_service.common.dto.request.RefreshRequest;
import com.tuannv78.identity_service.common.dto.response.AuthenticationResponse;
import com.tuannv78.identity_service.common.dto.response.IntrospectResponse;
import com.tuannv78.identity_service.common.enums.ErrorCodeEnum;
import com.tuannv78.identity_service.common.exception.AppException;
import com.tuannv78.repository.TokenRepository;
import com.tuannv78.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserRepository userRepository;
    TokenRepository tokenRepository;

    @NonFinal
    @Value("${jwt.signer-key}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.issuer}")
    protected String ISSUER;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Step 1: Verify whether the user exists in the Database
        User user = userRepository.findByUsername(request.getUsername())
                // Throw exception when the user is not exists
                .orElseThrow(() -> new AppException(ErrorCodeEnum.USER_NOT_EXISTED));

        // Step 2: Comparing two passwords with BCrypt Encryption
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword());

        // Step 3: Verifying the password whether is valid or not
        if (!authenticated)
            // Throw exception when the password not matches
            throw new AppException(ErrorCodeEnum.UNAUTHENTICATED);

        // Finally: Generating new token to the user
        return AuthenticationResponse.builder()
                .token(generateToken(user))
                .authenticated(true)
                .build();
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        // Step 1: init a isValid variable is true
        boolean isValid = true;

        // Step 2: try to call verifyToken() function without an error
        try {
            verifyToken(request.getToken(), false);
        } catch (Exception e) {
            // if any errors occur, isValid be false
            isValid = false;
        }

        // Finally: return response
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        // Step 1: Verify the old JWT token
        SignedJWT signedJWT = verifyToken(request.getToken(), true);

        // Step 2: get JWT ID, username, expiry time form old JWT token
        String jit = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        String username = signedJWT.getJWTClaimsSet().getSubject();

        // Step 3: Find the user, who assigned the JWT token
        User user = userRepository.findByUsername(username)
                // Throw an exception when this user can't be found
                .orElseThrow(() -> new AppException(ErrorCodeEnum.UNAUTHENTICATED));

        // Step 4: assign for this token is invalid
        tokenRepository.save(Token.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build()
        );

        // Finally: Generating new JWT Token
        return AuthenticationResponse.builder()
                .token(generateToken(user))
                .authenticated(true)
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            // Step 1: Verify the JWT token
            var signToken = verifyToken(request.getToken(), false);

            // Step 2: get JWT ID, expiry time form old JWT token
            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            // Finally: Assign for this token is invalid
            tokenRepository.save(Token.builder()
                    .id(jit)
                    .expiryTime(expiryTime)
                    .build()
            );
        } catch (AppException e) {
            log.info("Token Expired");
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {

        // Step 1: load signer key
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        // Step 2: Decode JWT token
        SignedJWT signedJWT = SignedJWT.parse(token);

        // Step 3: Get JWT expiry time
        Date expiryTime = isRefresh
                // if isRefresh == true => get expiry time from
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                // if isRefresh == false => get expiry time from claim
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        // Step 4: Verify whether request token is valid
        boolean isJWTValid = signedJWT.verify(verifier) && expiryTime.after(new Date());

        // Step 5: Verify whether user is logout the token
        boolean isLoggedOutToken = tokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID());

        // Step 6: Throw exception if any things in step 4,5 is unqualified
        if (!isJWTValid || isLoggedOutToken)
            throw new AppException(ErrorCodeEnum.UNAUTHENTICATED);

        // Finally: return JWT Object
        return signedJWT;
    }

    private String generateToken(User user) {
        // Step 1: Create header information
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // Step 2: Create JWT claims
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer(ISSUER)
                .issueTime(new Date())
                .expirationTime(
                        new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        // Step 3: Add claims into payload information
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        // Step 4: Create JWT Object
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            // Step 5: Signing JWT Token
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            // Finally: return JWT token
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        // Step 1: Create a string joiner statement
        StringJoiner stringJoiner = new StringJoiner(" ");

        // Step 2: If user's roles is not empty, then execute the block
        if (!CollectionUtils.isEmpty(user.getRoles()))
            // Loop into user's roles
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                // If role's permissions is not empty, then execute the block
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    // Loop into role's permissions
                    role.getPermissions().forEach(
                            permission -> stringJoiner.add(permission.getName())
                    );
            });

        // Finally: Return a string joined
        return stringJoiner.toString();
    }
}
