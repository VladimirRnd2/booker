package com.zuzex.booker.repository.dao;

import com.zuzex.booker.model.Book;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public Book findBookById(Long id) {
        return this.sqlSession.selectOne("findBookById", id);
    }

    public Book findBookByTitle(String title) {
        return this.sqlSession.selectOne("findBookByTitle", title);
    }

    public long saveBook(Book book) {
        return this.sqlSession.insert("saveBook", book);
    }

    public List<Long> getAllBooksId() {
        return this.sqlSession.selectList("getAllBooksId");
    }

    public Long getIdInBooks (long row) {
        return this.sqlSession.selectOne("getIdInBooks", row);
    }
    public Long getCountBooks() {
        return this.sqlSession.selectOne("getCountBooks");
    }


}
