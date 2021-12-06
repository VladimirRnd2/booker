package com.zuzex.booker.service;

import com.zuzex.booker.dto.BookRequest;
import com.zuzex.booker.dto.BookResponse;
import com.zuzex.booker.model.Author;
import com.zuzex.booker.model.Book;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookService {

    Book getBookById(Long id);

    Book getBookByTitle(String title);

    List<Book> getAllBooks();

    List<Book> getAllReadBooks();

    List<Book> getAllNoReadBooks();

    boolean addToReadBooks(Book book);

    boolean deleteFromReadBooks(Book book);

    List<Book> getBooksByDate(String date);

    BookResponse getBookResponse(BookRequest bookRequest, String token);

    Book createNewBook(BookResponse bookResponse);

    List<Book> getBooksByAuthor(Author author);

    Author getAuthorByName(String name);

    void deleteBookById(Long id);

    List<Author> getAllAuthors();

    Author getAuthorById(Long id);
}
