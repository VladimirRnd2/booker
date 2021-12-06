package com.zuzex.booker.service.impl;

import com.zuzex.booker.model.Author;
import com.zuzex.booker.model.Book;
import com.zuzex.booker.model.Status;
import com.zuzex.booker.rest.BookController;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebMvcTest(BookController.class)
class BookServiceImplTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookServiceImpl bookService;

    static Book firstBook;
    static Book secondBook;
    static Author author;

    @BeforeAll
    public static void init() {
        author = new Author();
        author.setId(1L);
        author.setName("Tanos");
        author.setBooks(null);
        author.setCreated(new Date());
        author.setUpdated(new Date());
        author.setStatus(Status.ACTIVE);

        firstBook = new Book();
        firstBook.setId(1L);
        firstBook.setTitle("Superman");
        firstBook.setDate("12.12.2020");
        firstBook.setAuthors(Arrays.asList(author));
        firstBook.setUsers(new ArrayList<>());
        firstBook.setCreated(new Date());
        firstBook.setUpdated(new Date());
        firstBook.setIsRead(false);
        firstBook.setStatus(Status.ACTIVE);

        secondBook = new Book();
        secondBook.setId(2L);
        secondBook.setTitle("Batman");
        secondBook.setDate("21.21.2012");
        secondBook.setAuthors(Arrays.asList(author));
        secondBook.setUsers(new ArrayList<>());
        secondBook.setCreated(new Date());
        secondBook.setUpdated(new Date());
        secondBook.setIsRead(false);
        secondBook.setStatus(Status.ACTIVE);
    }

    @Test
    void getBookById() {
    }

    @Test
    void getBookByTitle() {
    }

    @SneakyThrows
    @Test
    void getAllBooks() {
        when(bookService.getAllBooks()).thenReturn(Arrays.asList(firstBook,secondBook));

        this.mockMvc.perform(MockMvcRequestBuilders.get("user/book/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Superman"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].date").value("12.12.2020"));
    }

    @Test
    void getAllReadBooks() {
    }

    @Test
    void getAllNoReadBooks() {
    }

    @Test
    void addToReadBooks() {
    }

    @Test
    void deleteFromReadBooks() {
    }

    @Test
    void getBooksByDate() {
    }

    @Test
    void getBookResponse() {
    }

    @Test
    void createNewBook() {
    }

    @Test
    void getBooksByAuthor() {
    }

    @Test
    void getAuthorByName() {
    }

    @Test
    void deleteBookById() {
    }

    @Test
    void getAllAuthors() {
    }

    @Test
    void getAuthorById() {
    }
}