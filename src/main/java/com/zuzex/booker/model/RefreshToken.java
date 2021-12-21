package com.zuzex.booker.model;

import lombok.Data;


@Data
public class RefreshToken {

    private Long id;
    private String login;
    private String token;

    public RefreshToken(String login, String token) {
        this.login = login;
        this.token = token;
    }

    public RefreshToken() {
    }
}
