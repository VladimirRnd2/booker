package com.zuzex.booker.dto;

import lombok.Data;

@Data
public class AuthorResponse {

    private String name;

    public AuthorResponse(String name) {
        this.name = name;
    }

    public AuthorResponse() {
    }
}
