package com.zuzex.booker.repository.dao;

import com.zuzex.booker.dto.BookAuthorRequest;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BookAuthorDao {

    private final SqlSession sqlSession;

    @Autowired
    public BookAuthorDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public void addAuthorToBook(BookAuthorRequest request) {
        this.sqlSession.insert("addAuthorToBook", request);
    }
}
