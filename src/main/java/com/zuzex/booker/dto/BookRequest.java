package com.zuzex.booker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookRequest {

    @JsonProperty("title")
    private String title;

    public BookRequest(String title) {
        this.title = title;
    }

    public BookRequest() {
    }
}
