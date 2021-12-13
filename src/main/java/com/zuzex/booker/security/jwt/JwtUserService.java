package com.zuzex.booker.security.jwt;

import com.zuzex.booker.model.User;
import com.zuzex.booker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class JwtUserService implements UserDetailsService {

    private UserService userService;

    @Autowired
    public JwtUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public JwtUser loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByLogin(username);

        if(user == null) {
            throw new UsernameNotFoundException("User with login " + username + " not found.");
        }

        return JwtUser.fromUserToJwtUser(user);
    }
}
