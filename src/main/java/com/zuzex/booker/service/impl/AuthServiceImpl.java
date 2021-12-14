package com.zuzex.booker.service.impl;

import com.zuzex.booker.dto.AuthRefreshRequest;
import com.zuzex.booker.dto.AuthRequest;
import com.zuzex.booker.dto.AuthResponse;
import com.zuzex.booker.dto.RegistrationRequest;
import com.zuzex.booker.model.RefreshToken;
import com.zuzex.booker.model.User;
import com.zuzex.booker.repository.RefreshTokenRepository;
import com.zuzex.booker.security.jwt.JwtProv;
import com.zuzex.booker.service.AuthService;
import com.zuzex.booker.service.UserService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProv jwtProvider;

    @Autowired
    public AuthServiceImpl(UserService userService, RefreshTokenRepository refreshTokenRepository, JwtProv jwtProvider) {
        this.userService = userService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtProvider = jwtProvider;
    }


    @Override
    public User registerNewUser(RegistrationRequest request) {
        if(userService.findByLoginAndPassword(request.getLogin(), request.getPassword()) == null) {
            User user = new User();
            user.setLogin(request.getLogin());
            user.setPassword(request.getPassword());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());

            return userService.saveUser(user);
        }
        else {
            throw new RuntimeException("User with login " + request.getLogin() + "already exists" );
        }
    }

    @Override
    public AuthResponse auth(AuthRequest request) {
        User user = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String accessToken = jwtProvider.generateAccessToken(user.getLogin());
        String refreshToken = jwtProvider.generateRefreshToken(user.getLogin());
        refreshTokenRepository.save(new RefreshToken(user.getLogin(),refreshToken));
        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public AuthResponse refresh(AuthRefreshRequest request) {
        if(jwtProvider.validateRefreshToken(request.getToken())) {
            String login = jwtProvider.getLoginFromRefreshToken(request.getToken());
            Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByToken(request.getToken());
            if (optionalRefreshToken.isPresent()) {
                String saveRefreshToken = optionalRefreshToken.get().getToken();
                if(saveRefreshToken != null && saveRefreshToken.equals(request.getToken())) {
                    User user = userService.findByLogin(login);
                    String accessToken = jwtProvider.generateAccessToken(login);
                    String refreshToken = jwtProvider.generateRefreshToken(login);
                    refreshTokenRepository.deleteAll();
                    refreshTokenRepository.save(new RefreshToken(user.getLogin(),refreshToken));
                    return new AuthResponse(accessToken,refreshToken);
                }
            }
        }
        throw new JwtException("Invalid token");
    }
}
