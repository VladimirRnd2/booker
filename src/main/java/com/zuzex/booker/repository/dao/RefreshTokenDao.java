package com.zuzex.booker.repository.dao;

import com.zuzex.booker.model.RefreshToken;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RefreshTokenDao {

    private final SqlSession sqlSession;

    @Autowired
    public RefreshTokenDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public RefreshToken findRefreshTokenByToken(String token) {
        return this.sqlSession.selectOne("findRefreshTokenByToken", token);
    }

    public RefreshToken saveRefreshToken(RefreshToken refreshToken) {
        this.sqlSession.insert("saveRefreshToken", refreshToken);
        return refreshToken;
    }

    public void deleteRefreshTokenByLogin(String login) {
        this.sqlSession.delete("deleteRefreshTokenByLogin", login);
    }
}
