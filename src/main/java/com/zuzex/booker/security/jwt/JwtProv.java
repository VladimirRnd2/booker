package com.zuzex.booker.security.jwt;

public interface JwtProv {

    String generateAccessToken(String login);

    String generateRefreshToken(String login);

    boolean validateToken(String token);

    boolean validateRefreshToken(String token);

    String getLoginFromAccessToken(String token);

    String getLoginFromRefreshToken(String token);

}
