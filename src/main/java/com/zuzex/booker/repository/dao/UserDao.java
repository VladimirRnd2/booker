package com.zuzex.booker.repository.dao;

import com.zuzex.booker.model.User;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDao {

    private final SqlSession sqlSession;

    @Autowired
    public UserDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public List<User> findAllUsers(){
        return this.sqlSession.selectList("findAllUsers");
    }

    public List<User> getUsersByBookId(Long id) {
        return this.sqlSession.selectList("getUsersByBookId", id);
    }

    public User findUserById(Long id){
        return this.sqlSession.selectOne("findUserById", id);
    }

    public User findUserByLogin(String login) {
        return this.sqlSession.selectOne("findUserByLogin", login);
    }

    public long saveUser(User user) {
        return this.sqlSession.insert("saveUser", user);
    }

    public void deleteUserById(Long id) {
        this.sqlSession.delete("deleteUserById", id);
    }
}
