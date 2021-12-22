package com.zuzex.booker.service;

import com.zuzex.booker.model.Book;
import com.zuzex.booker.model.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);

    List<User> getAllUsers();

    User findByLogin(String login);

    User findById(Long id);

    void delete(Long id);

    User findByLoginAndPassword(String login, String password);

//    User addBookToUser(Book book, String token);
}
