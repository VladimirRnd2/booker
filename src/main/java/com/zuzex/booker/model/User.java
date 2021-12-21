package com.zuzex.booker.model;

import lombok.Data;

import java.util.List;


@Data
public class User extends BaseEntity{

    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private List<Book> books;
    private Role role;
}
