package com.zuzex.booker.repository.dao;

import com.zuzex.booker.model.Book;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookDao {

    private final SqlSession sqlSession;

    @Autowired
    public BookDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public List<Book> findAllBooks() {
        return this.sqlSession.selectList("findAllBooks");
    }

    public List<Book> findByIsReadEquals(Boolean isRead) {
        return this.sqlSession.selectList("findByIsReadEquals", isRead);
    }

    public List<Book> findByDate(String date) {
        return this.sqlSession.selectList("findByDate", date);
    }

    public List<Book> getBooksByUserId(Long id) {
        return this.sqlSession.selectList("getBooksByUserId", id);
    }

    public List<Book> getBooksByAuthorId(Long id) {
        return this.sqlSession.selectList("getBooksByAuthorId", id);
    }

    public Optional<Book> findBookById(Long id) {
        return this.sqlSession.selectOne("findBookById", id);
    }

    public Optional<Book> findBookByTitle(String title) {
        return this.sqlSession.selectOne("findBookByTitle", title);
    }

    public Book saveBook(Book book) {
        this.sqlSession.insert("saveBook");
        return book;
    }
}
