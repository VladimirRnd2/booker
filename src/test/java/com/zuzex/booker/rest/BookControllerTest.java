package com.zuzex.booker.rest;

import com.zuzex.booker.model.Author;
import com.zuzex.booker.model.Book;
import com.zuzex.booker.model.Status;
import com.zuzex.booker.security.jwt.JwtFilter;
import com.zuzex.booker.security.jwt.JwtProv;
import com.zuzex.booker.service.BookService;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.mockito.Mockito.when;

@WebMvcTest(BookController.class)
@ExtendWith(SpringExtension.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    @Qualifier("BookServiceImpl")
    private BookService bookService;

    @MockBean
    private JwtFilter jwtFilter;

    @MockBean
    @Qualifier("JwtProvider")
    private JwtProv jwtProvider;

    @MockBean
    @Qualifier("JwtUserService")
    private UserDetailsService jwtUserService;

    static Book firstBook;
    static Book secondBook;
    static Author author;

    @BeforeAll
    public static void init() {
        author = new Author();
        author.setId(1L);
        author.setName("Papich");
        author.setBooks(new ArrayList<>());
        author.setCreated(new Date());
        author.setUpdated(new Date());
        author.setStatus(Status.ACTIVE);

        firstBook = new Book();
        firstBook.setId(1L);
        firstBook.setTitle("Drow Ranger");
        firstBook.setDate("12.12.2020");
        firstBook.setAuthors(Arrays.asList(author));
        firstBook.setUsers(new ArrayList<>());
        firstBook.setIsRead(false);
        firstBook.setCreated(new Date());
        firstBook.setUpdated(new Date());
        firstBook.setStatus(Status.ACTIVE);

        secondBook = new Book();
        secondBook.setId(1L);
        secondBook.setTitle("Pudge");
        secondBook.setDate("21.21.2012");
        secondBook.setAuthors(Arrays.asList(author));
        secondBook.setUsers(new ArrayList<>());
        secondBook.setIsRead(false);
        secondBook.setCreated(new Date());
        secondBook.setUpdated(new Date());
        secondBook.setStatus(Status.ACTIVE);
    }

    @Test
    void createNewBook() {
    }

    @SneakyThrows
    @Test
    void getAllBooks() {
        when(bookService.getAllBooks()).thenReturn(Arrays.asList(firstBook,secondBook));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/user/book/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value( Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].title", Matchers.containsInAnyOrder("Drow Ranger", "Pudge")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].date", Matchers.containsInAnyOrder("12.12.2020","21.21.2012")));
    }

    @Test
    void getAllReadBooks() {
    }

    @Test
    void getAllNoReadBooks() {
    }

    @Test
    void getBookById() {
    }

    @Test
    void addToReadBooks() {
    }

    @Test
    void deleteFromReadBooks() {
    }

    @Test
    void getAllAuthorsByBookId() {
    }
}