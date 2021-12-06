package com.zuzex.booker.repository;

import com.zuzex.booker.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByTitle(String title);

    List<Book> findByIsReadEquals(boolean b);

    List<Book> findByDate(String date);

    Book findByTitleContaining(String title);
}
