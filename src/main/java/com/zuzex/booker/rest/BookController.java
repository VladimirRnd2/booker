package com.zuzex.booker.rest;


import com.zuzex.booker.dto.BookRequest;
import com.zuzex.booker.model.Author;
import com.zuzex.booker.model.Book;
import com.zuzex.booker.security.jwt.JwtFilter;
import com.zuzex.booker.service.BookService;
import com.zuzex.booker.service.impl.BookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping(value = "/user/book")
public class BookController {

    private BookService bookService;

    private JwtFilter jwtFilter;

    @Autowired
    public BookController(BookService bookService, JwtFilter jwtFilter) {
        this.bookService = bookService;
        this.jwtFilter = jwtFilter;
    }

    @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> createNewBook(@RequestBody BookRequest bookRequest, HttpServletRequest request) {
        Book book = bookService.createNewBook(bookService.getBookResponse(bookRequest, jwtFilter.getTokenFromRequest(request)));
        return new ResponseEntity<Book>(book, HttpStatus.CREATED);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Book> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return books;
    }

    @GetMapping(value = "/read", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Book> getAllReadBooks() {
        List<Book> books = bookService.getAllReadBooks();
        return books;
    }

    @GetMapping(value = "/noread", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Book> getAllNoReadBooks() {
        List<Book> books = bookService.getAllNoReadBooks();
        return books;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Book getBookById(@PathVariable(name = "id") Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping(value = "/{id}/isread")
    public boolean addToReadBooks(@PathVariable(name = "id") Long id) {
        return bookService.addToReadBooks(bookService.getBookById(id));
    }

    @GetMapping(value = "/{id}/isnoread")
    public void deleteFromReadBooks(@PathVariable(name = "id")Long id) {
        bookService.deleteFromReadBooks(bookService.getBookById(id));
    }

    @GetMapping(value = "/{id}/authors", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Author> getAllAuthorsByBookId(@PathVariable(name = "id") Long id){
        return bookService.getBookById(id).getAuthors();
    }



}
