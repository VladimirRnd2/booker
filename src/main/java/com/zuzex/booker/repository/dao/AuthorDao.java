package com.zuzex.booker.repository.dao;

import com.zuzex.booker.model.Author;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthorDao {

    private final SqlSession sqlSession;

    @Autowired
    public AuthorDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public List<Author> findAllAuthors() {
       return this.sqlSession.selectList("findAllAuthors");
    }

    public List<Author> getAuthorsByBookId(Long id) {
        return this.sqlSession.selectList("getAuthorsByBookId", id);
    }

    public Optional<Author> findAuthorById(Long id) {
        return this.sqlSession.selectOne("findAuthorById", id);
    }

    public Optional<Author> findAuthorByName(String name) {
        return this.sqlSession.selectOne("findAuthorByName", name);
    }

    public Author saveAuthor(Author author) {
        this.sqlSession.insert("saveAuthor");
        return author;
    }
}
