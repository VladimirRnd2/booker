package com.zuzex.booker.service.impl;

import com.zuzex.booker.model.Book;
import com.zuzex.booker.model.Role;
import com.zuzex.booker.model.Status;
import com.zuzex.booker.model.User;
import com.zuzex.booker.repository.RoleRepository;
import com.zuzex.booker.repository.UserRepository;
import com.zuzex.booker.security.jwt.JwtProv;
import com.zuzex.booker.security.jwt.JwtProvider;
import com.zuzex.booker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtProv jwtProvider;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, JwtProv jwtProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public User saveUser(User user) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        user.setRole(roleUser);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getCreated() == null) {
            user.setCreated(new Date());
        }
        user.setUpdated(new Date());
        user.setStatus(Status.ACTIVE);
        user.setBooks(new ArrayList<Book>());
        return userRepository.save(user);
    }



    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public User findById(Long id) {
        User result = userRepository.findById(id).orElse(null);

        if( result == null) {
            return null;
        }
        else {
            return result;
        }
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public User findByLoginAndPassword(String login, String password) {
        User user = findByLogin(login);
        if(user != null) {
            if(passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User addBookToUser(Book book, String token) {
        User user = findByLogin(jwtProvider.getLoginFromToken(token));
        if(!user.getBooks().contains(book)) {
            user.getBooks().add(book);
            userRepository.save(user);
        }
        return user;
    }
}
