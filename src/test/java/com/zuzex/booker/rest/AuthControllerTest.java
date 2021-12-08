package com.zuzex.booker.rest;

import com.zuzex.booker.model.Role;
import com.zuzex.booker.model.Status;
import com.zuzex.booker.model.User;
import com.zuzex.booker.security.jwt.JwtProv;
import com.zuzex.booker.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean

    private UserService userService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private JwtProv jwtProvider;

    User user;

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
    }

    @Test
    void shouldCreateMockMvc() {
        assertNotNull(mockMvc);
    }

    @Test
    void shouldReturnListUsers() throws Exception {

        when(userService.getAllUsers()).thenReturn(List.of(user));

        this.mockMvc.perform(get("/allusers")
                        .with(user("vova").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$[0].login").value("login"))
                .andExpect(jsonPath("$[0].firstName").value("firstName"));

    }

    @Test
    void shouldCreateUser() throws Exception {
        this.mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"test1\",\"password\":\"test1\",\"firstName\":\"test1\",\"lastName\":\"test1\"}"))
                .andExpect(status().isCreated());

        verify(userService).saveUser(any(User.class));

    }


    @Test
    void shouldAuthUser() throws Exception {

        when(userService.findByLoginAndPassword(anyString(),anyString())).thenReturn(user);
        this.mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"test1\",\"password\":\"test1\"}"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(jwtProvider).generateToken(user.getLogin());
        verify(jwtProvider).generateRefreshToken(user.getLogin());
    }
//
    @Test
    void refresh() throws Exception {

        when(jwtProvider.getLoginFromToken(anyString())).thenReturn(user.getLogin());
        when(userService.findByLogin(user.getLogin())).thenReturn(user);

        this.mockMvc.perform(post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" + "    \"token\":\"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0MSIsImV4cCI6MTYzODUzMjMyMn0.qiexiCwnfJ4xJ69TIOQer2raqEzM-BKagM8VuMqxagjpwNwtItSy9wq1oTVX2ua7tlP1S40Ee2wZduHGB-2x1Q\"\n" + "}"))
                .andExpect(status().isOk());

        verify(jwtProvider).generateToken(user.getLogin());
        verify(jwtProvider).generateRefreshToken(user.getLogin());
    }
}