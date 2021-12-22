package com.zuzex.booker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserBookRequest {
    private Long userId;
    private Long bookId;
}
