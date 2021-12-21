package com.zuzex.booker.service.impl;

import com.zuzex.booker.model.Book;
import com.zuzex.booker.model.Role;
import com.zuzex.booker.model.Status;
import com.zuzex.booker.model.User;
import com.zuzex.booker.repository.dao.RoleDao;
import com.zuzex.booker.repository.dao.UserDao;
import com.zuzex.booker.security.jwt.JwtProv;
import com.zuzex.booker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserDao userDao;
    private RoleDao roleDao;
    private PasswordEncoder passwordEncoder;
    private JwtProv jwtProvider;

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao, JwtProv jwtProvider) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public User saveUser(User user) {
        Role roleUser = roleDao.findRoleByName("ROLE_USER");
        user.setRole(roleUser);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getCreated() == null) {
            user.setCreated(new Date());
        }
        user.setUpdated(new Date());
        user.setStatus(Status.ACTIVE);
        user.setBooks(new ArrayList<Book>());
        userDao.saveUser(user);
        return user;
    }



    @Override
    public List<User> getAllUsers() {
        List<User> users = userDao.findAllUsers();
        return users;
    }

    @Override
    public User findByLogin(String login) {
        return userDao.findUserByLogin(login);
    }

    @Override
    public User findById(Long id) {
        User result = userDao.findUserById(id);
        return result;
    }

    @Override
    public void delete(Long id) {
        userDao.deleteUserById(id);
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
        User user = findByLogin(jwtProvider.getLoginFromAccessToken(token));
        if(!user.getBooks().contains(book)) {
            user.getBooks().add(book);
            userDao.saveUser(user);
        }
        return user;
    }
}
