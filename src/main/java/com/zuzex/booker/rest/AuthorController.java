package com.zuzex.booker.rest;

import com.zuzex.booker.model.Author;
import com.zuzex.booker.model.Book;
import com.zuzex.booker.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(value = "/user/author")
@Validated
public class AuthorController {

    private BookService bookService;

    @Autowired
    public AuthorController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Author> getAllAuthors () {
        return bookService.getAllAuthors();
    }

    @GetMapping(value = "/{id}")
    public Author getAuthorById(@PathVariable(name = "id")@Min(0) Long id) {
        return bookService.getAuthorById(id);
    }

    @GetMapping(value = "/{id}/books")
    public List<Book> getAllBookByAuthorId(@PathVariable(name = "id") @Min(0) Long id) {
        return bookService.getAuthorById(id).getBooks();
    }
}
