package com.zuzex.booker.rest;

import com.zuzex.booker.model.Author;
import com.zuzex.booker.model.Book;
import com.zuzex.booker.model.Status;
import com.zuzex.booker.service.BookService;
import liquibase.pro.packaged.D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthorControllerTest {

    @Autowired
    private WebApplicationContext context;

    MockMvc mockMvc;

    Author author;
    Book firstBook;
    Book secondBook;

    @MockBean
    private BookService bookService;


    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        author = new Author();
        author.setId(1L);
        author.setName("Duma");
        author.setBooks(new ArrayList<>());
        author.setCreated(new Date());
        author.setUpdated(new Date());
        author.setStatus(Status.ACTIVE);

        firstBook = new Book();
        firstBook.setId(1L);
        firstBook.setTitle("Drow Ranger");
        firstBook.setDate("12.12.2020");
        firstBook.setIsRead(false);
        firstBook.setAuthors(null);
        firstBook.setUsers(null);
        firstBook.setCreated(new Date());
        firstBook.setUpdated(new Date());
        firstBook.setStatus(Status.ACTIVE);

        secondBook = new Book();
        secondBook.setId(1L);
        secondBook.setTitle("Pudge");
        secondBook.setDate("21.21.2012");
        secondBook.setIsRead(false);
        secondBook.setAuthors(null);
        secondBook.setUsers(null);
        secondBook.setCreated(new Date());
        secondBook.setUpdated(new Date());
        secondBook.setStatus(Status.ACTIVE);

        author.getBooks().add(firstBook);
        author.getBooks().add(secondBook);
    }

    @Test
    void shouldCreateMockMvc() {
        assertNotNull(mockMvc);
    }

    @Test
    void getAllAuthors() throws Exception {
        when(bookService.getAllAuthors()).thenReturn(List.of(author));

        this.mockMvc.perform(get("/user/author/")
                .with(user("vova").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Duma"))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getAuthorById() throws Exception {
        when(bookService.getAuthorById(1L)).thenReturn(author);

        this.mockMvc.perform(get("/user/author/1")
                .with(user("vova").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Duma"));
    }

    @Test
    void getAllBookByAuthorId() throws Exception {
        when(bookService.getAuthorById(1L)).thenReturn(author);

        this.mockMvc.perform(get("/user/author/1/books")
                .with(user("vova").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Drow Ranger"))
                .andExpect(jsonPath("$[1].title").value("Pudge"));
    }
}