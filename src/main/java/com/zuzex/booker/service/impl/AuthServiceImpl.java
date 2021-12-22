package com.zuzex.booker.service.impl;

import com.zuzex.booker.dto.AuthRefreshRequest;
import com.zuzex.booker.dto.AuthRequest;
import com.zuzex.booker.dto.AuthResponse;
import com.zuzex.booker.dto.RegistrationRequest;
import com.zuzex.booker.model.RefreshToken;
import com.zuzex.booker.model.User;
import com.zuzex.booker.repository.dao.RefreshTokenDao;
import com.zuzex.booker.security.jwt.JwtProv;
import com.zuzex.booker.service.AuthService;
import com.zuzex.booker.service.UserService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final RefreshTokenDao refreshTokenDao;
    private final JwtProv jwtProvider;

    @Autowired
    public AuthServiceImpl(UserService userService, RefreshTokenDao refreshTokenDao, JwtProv jwtProvider) {
        this.userService = userService;
        this.refreshTokenDao = refreshTokenDao;
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
        refreshTokenDao.saveRefreshToken(new RefreshToken(user.getLogin(),refreshToken));
        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public AuthResponse refresh(AuthRefreshRequest request) {
        if(jwtProvider.validateRefreshToken(request.getToken())) {
            String login = jwtProvider.getLoginFromRefreshToken(request.getToken());
            RefreshToken optionalRefreshToken = refreshTokenDao.findRefreshTokenByToken(request.getToken());
            if (optionalRefreshToken != null) {
                String saveRefreshToken = optionalRefreshToken.getToken();
                if(saveRefreshToken != null && saveRefreshToken.equals(request.getToken())) {
                    User user = userService.findByLogin(login);
                    String accessToken = jwtProvider.generateAccessToken(login);
                    String refreshToken = jwtProvider.generateRefreshToken(login);
                    refreshTokenDao.deleteRefreshTokenByLogin(login);
                    refreshTokenDao.saveRefreshToken(new RefreshToken(user.getLogin(),refreshToken));
                    return new AuthResponse(accessToken,refreshToken);
                }
            }
        }
        throw new JwtException("Invalid token");
    }
}
