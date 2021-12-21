package com.zuzex.booker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;


@Data
public class Author extends BaseEntity{

    private String name;
    @JsonIgnore
    private List<Book> books;
}
