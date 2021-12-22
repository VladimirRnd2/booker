package com.zuzex.booker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookAuthorRequest {
    Long bookId;
    Long authorId;
}
