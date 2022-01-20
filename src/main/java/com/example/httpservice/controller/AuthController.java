package com.example.httpservice.controller;

import com.example.httpservice.api.request.RegisterRequest;
import com.example.httpservice.api.response.AuthData;
import com.example.httpservice.api.response.DataResponse;
import com.example.httpservice.exceptions.AuthenticationException;
import com.example.httpservice.exceptions.InvalidInputFormat;
import com.example.httpservice.exceptions.PersonExistException;
import com.example.httpservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneId;

@RestController
@Tag(name = "Контроллер для работы с аккаунтом")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Регистрация")
    @PostMapping("/register")
    public ResponseEntity<DataResponse<AuthData>> registration(@RequestBody RegisterRequest registerRequest) throws PersonExistException, InvalidInputFormat {
        return new ResponseEntity<>(authService.registration(registerRequest), HttpStatus.OK);
    }

    @Operation(summary = "Авторизация")
    @PostMapping("/login")
    public ResponseEntity<DataResponse<AuthData>> login(@RequestBody RegisterRequest registerRequest) throws AuthenticationException {
        return new ResponseEntity<>(authService.login(registerRequest), HttpStatus.OK);
    }

    @Operation(summary = "Выход из сессии")
    @GetMapping("/logout")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<DataResponse<?>> getLogout() {
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(new DataResponse<>()
                .setTimestamp(Instant.from(Instant.now().atZone(ZoneId.systemDefault()))), HttpStatus.OK);
    }
}
