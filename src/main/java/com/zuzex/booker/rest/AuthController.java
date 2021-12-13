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
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private JwtProv jwtProvider;
//
//    @GetMapping(value = "/allusers")
//    public List<User> getAllUsers() {
//        return userService.getAllUsers();
//    }

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody @Valid RegistrationRequest request) {

//        if(userService.findByLogin(request.getLogin()) != null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        User user = new User();
//        user.setLogin(request.getLogin());
//        user.setPassword(request.getPassword());
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//
//        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
        User user = authService.registerNewUser(request);
        return new ResponseEntity<>(user,HttpStatus.CREATED);
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> auth(@RequestBody @Valid AuthRequest request) {
//        User user = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
//        String accessToken = jwtProvider.generateAccessToken(user.getLogin());
//        String refreshToken = jwtProvider.generateRefreshToken(user.getLogin());
//        return new ResponseEntity<>(new AuthResponse(accessToken, refreshToken), HttpStatus.OK);
        AuthResponse response = authService.auth(request);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody @Valid AuthRefreshRequest request) {
//        if(jwtProvider.validateRefreshToken(request.getToken())) {
//            String login = jwtProvider.getLoginFromRefreshToken(request.getToken());
//            User user = userService.findByLogin(login);
//            String accessToken = jwtProvider.generateAccessToken(login);
//            String refreshToken = jwtProvider.generateRefreshToken(login);
//
//            return new ResponseEntity<>(new AuthResponse(accessToken, refreshToken), HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        AuthResponse response = authService.refresh(request);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
