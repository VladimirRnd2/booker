package com.zuzex.booker.security.jwt;

public interface JwtProv {
    String generateToken(String login);

    String generateRefreshToken(String login);

    boolean validateToken(String token);

    String getLoginFromToken(String token);

}
