package com.zuzex.booker.service.impl;

import com.zuzex.booker.model.*;
import com.zuzex.booker.repository.RoleRepository;
import com.zuzex.booker.repository.UserRepository;
import com.zuzex.booker.security.jwt.JwtProv;
import com.zuzex.booker.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private JwtProv jwtProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    User user;
    Book firstBook;
    Author author;

    @BeforeEach
    void setup() {

        user = new User();
        user.setId(1L);
        user.setLogin("login");
        user.setPassword("password");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setBooks(new ArrayList<>());
        user.setRole(new Role(1L, "ROLE_USER"));
        user.setStatus(Status.ACTIVE);
        user.setCreated(new Date());
        user.setUpdated(new Date());

        author = new Author();
        author.setId(1L);
        author.setName("Duma");
        author.setBooks(new ArrayList<>());
        author.setCreated(new Date());
        author.setUpdated(new Date());
        author.setStatus(Status.ACTIVE);

        firstBook = new Book();
        firstBook.setId(1L);
        firstBook.setTitle("Abba");
        firstBook.setDate("12.12.2020");
        firstBook.setIsRead(false);
        firstBook.setAuthors(List.of(author));
        firstBook.setUsers(null);
        firstBook.setCreated(new Date());
        firstBook.setUpdated(new Date());
        firstBook.setStatus(Status.ACTIVE);

    }

    @Test
    void saveUser() {

        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");

        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        Mockito.when(roleRepository.findByName("ROLE_USER")).thenReturn(role);

        User result = userService.saveUser(user);

        Assertions.assertEquals(user, result);

    }

    @Test
    void getAllUsers() {

        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> userList = userService.getAllUsers();

        Assertions.assertEquals(user, userList.get(0));
        Assertions.assertEquals(userList.size(), 1);
    }

    @Test
    void findByLogin() {
        Mockito.when(userRepository.findByLogin("login")).thenReturn(user);

        User result = userService.findByLogin("login");

        Assertions.assertEquals(user,result);
    }

    @Test
    void findById() {

        Mockito.when(userRepository.findById(1L)).thenReturn(ofNullable(user));
        User result = userService.findById(1L);

        Assertions.assertEquals(user,result);
    }

    @Test
    void delete() {

        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");

        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        Mockito.when(roleRepository.findByName("ROLE_USER")).thenReturn(role);

        User result = userService.saveUser(user);

        user.setPassword(passwordEncoder.encode("password"));

        Assertions.assertEquals(user, result);

        userService.delete(user.getId());

        Assertions.assertNotEquals(user, userRepository.findByLogin(user.getLogin()));

    }

    @Test
    void findByLoginAndPassword() {

        user.setPassword(passwordEncoder.encode("password"));

        Mockito.when(userRepository.findByLogin("login")).thenReturn(user);

        User result = userService.findByLoginAndPassword(user.getLogin(), "password");

        Assertions.assertEquals(user,result);
    }

    @Test
    void addBookToUser() {

        Mockito.when(jwtProvider.getLoginFromAccessToken(any(String.class))).thenReturn("login");
        Mockito.when(userRepository.findByLogin("login")).thenReturn(user);

        User result = userService.findByLogin("login");
        userService.addBookToUser(firstBook,"token");

        Assertions.assertEquals(firstBook, result.getBooks().get(0));
    }
}