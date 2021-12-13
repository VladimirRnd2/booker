package com.zuzex.booker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class AuthResponse {

    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;
}
