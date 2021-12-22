package com.zuzex.booker.repository.dao;

import com.zuzex.booker.dto.UserBookRequest;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserBookDao {

    private final SqlSession sqlSession;

    @Autowired
    public UserBookDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public void addBookToUser(UserBookRequest request) {
        this.sqlSession.insert("addBookToUser", request);
    }
}
