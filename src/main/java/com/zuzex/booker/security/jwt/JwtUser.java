package com.zuzex.booker.security.jwt;

import com.zuzex.booker.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


public class JwtUser implements UserDetails {

    private Long id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private Collection<? extends GrantedAuthority> authorities;

    public static JwtUser fromUserToJwtUser(User user) {
        JwtUser jwtUser = new JwtUser();
        jwtUser.login = user.getLogin();
        jwtUser.password = user.getPassword();
        jwtUser.firstName = user.getFirstName();
        jwtUser.lastName = user.getLastName();
        jwtUser.authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName()));

        return jwtUser;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Long getId() {
        return id;
    }



}
