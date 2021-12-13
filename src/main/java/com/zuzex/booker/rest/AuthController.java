package com.zuzex.booker.rest;

import com.zuzex.booker.dto.AuthRefreshRequest;
import com.zuzex.booker.dto.AuthRequest;
import com.zuzex.booker.dto.AuthResponse;
import com.zuzex.booker.dto.RegistrationRequest;
import com.zuzex.booker.model.User;
import com.zuzex.booker.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody @Valid RegistrationRequest request) {
        User user = authService.registerNewUser(request);
        return new ResponseEntity<>(user,HttpStatus.CREATED);
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> auth(@RequestBody @Valid AuthRequest request) {
        AuthResponse response = authService.auth(request);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody @Valid AuthRefreshRequest request) {
        AuthResponse response = authService.refresh(request);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
