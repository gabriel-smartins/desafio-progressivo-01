package com.github.gabrielsmartins.taskmanager.controller;

import com.github.gabrielsmartins.taskmanager.controller.dto.request.AuthRequest;
import com.github.gabrielsmartins.taskmanager.controller.dto.request.RefreshTokenRequest;
import com.github.gabrielsmartins.taskmanager.controller.dto.request.RegisterRequest;
import com.github.gabrielsmartins.taskmanager.controller.dto.response.AuthResponse;
import com.github.gabrielsmartins.taskmanager.model.RefreshToken;
import com.github.gabrielsmartins.taskmanager.model.User;
import com.github.gabrielsmartins.taskmanager.service.AuthService;
import com.github.gabrielsmartins.taskmanager.service.RefreshTokenService;
import com.github.gabrielsmartins.taskmanager.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.token.expiration-in-seconds}")
    private Integer expirationTokenInSeconds;

    @Value("${jwt.refresh-token.expiration-in-seconds}")
    private Integer expirationRefreshTokenInSeconds;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest request) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var user = (User) auth.getPrincipal();

        var token = tokenService.generateToken(user, expirationTokenInSeconds);
        var refreshTokenEntity = refreshTokenService.save(user, expirationRefreshTokenInSeconds);

        return ResponseEntity.ok(new AuthResponse(token, refreshTokenEntity.getToken()));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest request) {
        String encryptedPassword = passwordEncoder.encode(request.password());

        User newUser = new User(request.name(), request.email(), encryptedPassword, request.role());

        authService.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        return refreshTokenService.findByToken(request.refreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {

                    String newAccessToken = tokenService.generateToken(user, expirationTokenInSeconds);

                    return ResponseEntity.ok(new AuthResponse(newAccessToken, request.refreshToken()));
                })
                .orElseThrow(() -> new RuntimeException("Refresh Token inválido ou não encontrado."));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid RefreshTokenRequest request) {
        refreshTokenService.findByToken(request.refreshToken())
                .ifPresent(refreshTokenService::delete);

        return ResponseEntity.noContent().build();
    }
}
