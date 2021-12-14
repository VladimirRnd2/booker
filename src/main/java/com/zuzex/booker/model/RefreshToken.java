package com.zuzex.booker.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tokens")
@Data
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "token")
    private String token;

    public RefreshToken(String login, String token) {
        this.login = login;
        this.token = token;
    }

    public RefreshToken() {
    }
}
