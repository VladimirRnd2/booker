package com.zuzex.booker.mapper;

import com.zuzex.booker.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {

    List<User> findAllUsers();
    List<User> getUsersByBookId(@Param("id") Long id);
    User findUserById(@Param("id") Long id);
    User findUserByLogin(@Param("login") String login);
    void saveUser(User user);
    void deleteUserById(@Param("id") Long id);

}