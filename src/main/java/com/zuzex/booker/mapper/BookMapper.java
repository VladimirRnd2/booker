package com.zuzex.booker.mapper;

import com.zuzex.booker.model.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookMapper {


    Book findBookById(@Param("id") Long id);
    Book findBookByTitle(@Param("title") String title);
    List<Book> findAllBooks();
    List<Book> findByIsReadEquals(@Param("isRead") Boolean isRead);
    List<Book> findByDate(@Param("date") String date);
    List<Book> getBooksByUserId(@Param("id") Long id);
    List<Book> getBooksByAuthorId(@Param("id") Long id);
    Long saveBook(Book book);
}
