package com.zuzex.booker.rest;

import com.zuzex.booker.dto.BookRequest;
import com.zuzex.booker.dto.BookResponse;
import com.zuzex.booker.model.Author;
import com.zuzex.booker.model.Book;
import com.zuzex.booker.model.Status;
import com.zuzex.booker.security.jwt.JwtFilter;
import com.zuzex.booker.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private JwtFilter jwtFilter;

    Book firstBook;
    Book secondBook;
    Author author;

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
        firstBook.setTitle("Abba");
        firstBook.setDate("12.12.2020");
        firstBook.setIsRead(false);
        firstBook.setAuthors(List.of(author));
        firstBook.setUsers(null);
        firstBook.setCreated(new Date());
        firstBook.setUpdated(new Date());
        firstBook.setStatus(Status.ACTIVE);

        secondBook = new Book();
        secondBook.setId(2L);
        secondBook.setTitle("Pudge");
        secondBook.setDate("21.21.2012");
        secondBook.setIsRead(true);
        secondBook.setAuthors(null);
        secondBook.setUsers(null);
        secondBook.setCreated(new Date());
        secondBook.setUpdated(new Date());
        secondBook.setStatus(Status.ACTIVE);
    }

    @Test
    void shouldCreateMockMvc() {
        assertNotNull(mockMvc);
    }

    @Test
    void createNewBook() throws Exception {

        BookResponse bookResponse = new BookResponse();
        bookResponse.setTitle("Abba");
        bookResponse.setDate("12.12.2020");
        bookResponse.setAuthors(new ArrayList<>());
        bookResponse.setToken("newToken");

        when(bookService.createNewBook(any(BookResponse.class))).thenReturn(firstBook);
        when(bookService.getBookResponse(any(BookRequest.class))).thenReturn(bookResponse);

        this.mockMvc.perform(post("/user/book/search")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content("{\"title\": \"Abba\"}")
                .with(user("vova").roles("USER")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Abba"));

//        verify(bookService).getBookResponse(any(BookRequest.class));
        verify(bookService).createNewBook(any(BookResponse.class));
    }

    @Test
    void getAllBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of(firstBook,secondBook));

        this.mockMvc.perform(get("/user/book/").characterEncoding(StandardCharsets.UTF_8)
                .with(user("vova").roles("USER")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Abba"))
                .andExpect(jsonPath("$[1].title").value("Pudge"))
                .andExpect(jsonPath("$[0].date").value("12.12.2020"))
                .andExpect(jsonPath("$[1].date").value("21.21.2012"));
    }

    @Test
    void getAllReadBooks() throws Exception {
        when(bookService.getAllReadBooks()).thenReturn(List.of(secondBook));

        this.mockMvc.perform(get("/user/book/read")
                .with(user("vova").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].date").value("21.21.2012"))
                .andExpect(jsonPath("$[0].isRead").value(true))
                .andExpect(jsonPath("$[0].title").value("Pudge"));
    }

    @Test
    void getAllNoReadBooks() throws Exception {
        when(bookService.getAllNoReadBooks()).thenReturn(List.of(firstBook));

        this.mockMvc.perform(get("/user/book/noread")
                        .with(user("vova").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].date").value("12.12.2020"))
                .andExpect(jsonPath("$[0].isRead").value(false))
                .andExpect(jsonPath("$[0].title").value("Abba"));
    }

    @Test
    void getBookById() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(firstBook);

        this.mockMvc.perform(get("/user/book/1")
                .with(user("vova").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value("12.12.2020"))
                .andExpect(jsonPath("$.isRead").value(false))
                .andExpect(jsonPath("$.title").value("Abba"));
    }

    @Test
    void addToReadBooks() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(firstBook);

        this.mockMvc.perform(get("/user/book/1/isread")
                .with(user("vova").roles("USER")))
                .andExpect(status().isOk());


    }

    @Test
    void deleteFromReadBooks() throws Exception {
        when(bookService.getBookById(2L)).thenReturn(secondBook);

        this.mockMvc.perform(get("/user/book/2/isnoread")
                        .with(user("vova").roles("USER")))
                .andExpect(status().isOk());
    }

    @Test
    void getAllAuthorsByBookId() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(firstBook);

        this.mockMvc.perform(get("/user/book/1/authors")
                .with(user("vova").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Duma"));
    }

    @Test
    void deleteBookById() throws Exception {

        this.mockMvc.perform(delete("/user/book/1")
                .with(user("vova").roles("USER")))
                .andExpect(status().isOk());
    }
}