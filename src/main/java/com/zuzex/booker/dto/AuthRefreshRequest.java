package com.zuzex.booker.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AuthRefreshRequest {

    @NotEmpty
    private String token;
}
