package com.zuzex.booker.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zuzex.booker.dto.AuthorResponse;
import com.zuzex.booker.dto.BookRequest;
import com.zuzex.booker.dto.BookResponse;
import com.zuzex.booker.dto.serializer.BookResponseDeserializer;
import com.zuzex.booker.model.Author;
import com.zuzex.booker.model.Book;
import com.zuzex.booker.model.Status;
import com.zuzex.booker.repository.dao.AuthorDao;
import com.zuzex.booker.repository.dao.BookDao;
import com.zuzex.booker.security.jwt.JwtFilter;
import com.zuzex.booker.service.BookService;
import com.zuzex.booker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    private BookDao bookDao;
    private AuthorDao authorDao;
    private RestTemplate restTemplate;
    private UserService userService;
    private JwtFilter jwtFilter;


    @Autowired
    public BookServiceImpl(BookDao bookDao, AuthorDao authorDao, RestTemplate restTemplate, UserService userService, JwtFilter jwtFilter) {
        this.bookDao = bookDao;
        this.authorDao = authorDao;
        this.restTemplate = restTemplate;
        this.userService = userService;
        this.jwtFilter = jwtFilter;
    }

    @Override
    public Book getBookById(Long id) {
        Optional<Book> book = bookDao.findBookById(id);
        if(book.isPresent() && book.get().getStatus() == Status.ACTIVE) {
            return book.get();
        }
        else
            throw new RuntimeException("Book with id " + id + " not found");
    }

    @Override
    public Book getBookByTitle(String title) {
        Optional<Book> book = bookDao.findBookByTitle(title);
        if(book.isPresent() && book.get().getStatus() == Status.ACTIVE) {
            return book.get();
        }
        else
            throw new RuntimeException("Book with id " + title + " not found");
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> bookList = bookDao.findAllBooks();
        List<Book> resultList = new ArrayList<>();

        for (Book book : bookList) {
            if(book.getStatus() == Status.ACTIVE)
                resultList.add(book);
        }
        return resultList;
    }

    @Override
    public List<Book> getAllReadBooks() {
        List<Book> bookList = bookDao.findByIsReadEquals(true);
        List<Book> resultList = new ArrayList<>();

        for (Book book : bookList) {
            if(book.getStatus() == Status.ACTIVE)
                resultList.add(book);
        }
        return resultList;
    }

    @Override
    public List<Book> getAllNoReadBooks() {
        List<Book> bookList = bookDao.findByIsReadEquals(false);
        List<Book> resultList = new ArrayList<>();

        for (Book book : bookList) {
            if(book.getStatus() == Status.ACTIVE)
                resultList.add(book);
        }
        return resultList;
    }

    @Override
    public boolean addToReadBooks(Book book) {
        if(book.getIsRead())
            return false;
        book.setIsRead(true);
        bookDao.saveBook(book);
        return true;
    }

    @Override
    public boolean deleteFromReadBooks(Book book) {
        if(!book.getIsRead())
            return false;
        book.setIsRead(false);
        bookDao.saveBook(book);
        return true;
    }

    @Override
    public List<Book> getBooksByDate(String date) {
        List<Book> bookList = bookDao.findByDate(date);
        List<Book> resultList = new ArrayList<>();

        for (Book book : bookList) {
            if(book.getStatus() == Status.ACTIVE)
                resultList.add(book);
        }
        return resultList;
    }

    @Override
    public BookResponse getBookResponse(BookRequest bookRequest, HttpServletRequest request) {
        String responseEntity = restTemplate.getForObject(BASE_URL.concat(bookRequest.getTitle()),String.class);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(BookResponse.class, new BookResponseDeserializer())
                .create();
        BookResponse bookResponse = gson.fromJson(responseEntity,BookResponse.class);
        bookResponse.setToken(jwtFilter.getTokenFromRequest(request));
        return bookResponse;
    }

    @Override
    public Book createNewBook(BookResponse bookResponse) {
        Optional<Book> optionalBook = bookDao.findBookByTitle(bookResponse.getTitle());

        if(optionalBook.isEmpty()) {
            Book book = new Book();
            book.setTitle(bookResponse.getTitle());
            book.setDate(bookResponse.getDate());
            book.setAuthors(getAllAuthorsFromAuthorResponse(bookResponse));
            book.setIsRead(false);
            book.setCreated(new Date());
            book.setUpdated(new Date());
            book.setStatus(Status.ACTIVE);

            bookDao.saveBook(book);
            userService.addBookToUser(book, bookResponse.getToken());
            return book;
        }

            optionalBook.get().setStatus(Status.ACTIVE);
            bookDao.saveBook(optionalBook.get());
            userService.addBookToUser(optionalBook.get(), bookResponse.getToken());
            return optionalBook.get();
    }

    @Override
    public List<Book> getBooksByAuthor(Author author) {
        return author.getBooks();
    }

    @Override
    public Author getAuthorByName(String name) {
        Optional<Author> optionalAuthor = authorDao.findAuthorByName(name);
        if(optionalAuthor.isPresent())
            return optionalAuthor.get();
        else
            throw new RuntimeException("Author with this name not found");
    }

    @Override
    public void deleteBookById(Long id) {
//        Book book = bookRepository.findById(id).orElse(null);
//        book.setStatus(Status.DELETED); // будет NPE , устранить
//        book.setUpdated(new Date());
//        bookRepository.save(book);

        Optional<Book> book = bookDao.findBookById(id);
        if(book.isPresent()) {
            book.get().setStatus(Status.DELETED);
            book.get().setUpdated(new Date());
            bookDao.saveBook(book.get());
        }
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorDao.findAllAuthors();
    }

    @Override
    public Author getAuthorById(Long id) {
        return authorDao.findAuthorById(id).orElse(null);
    }

    private List<Author> getAllAuthorsFromAuthorResponse(BookResponse bookResponse) {
            List<Author> authors = new ArrayList<>();
            for (AuthorResponse response : bookResponse.getAuthors()) {
                Optional<Author> optionalAuthor = authorDao.findAuthorByName(response.getName());
                if(optionalAuthor.isEmpty()) {

                    Author author = new Author();
                    author.setName(response.getName());
                    author.setCreated(new Date());
                    author.setUpdated(new Date());
                    author.setStatus(Status.ACTIVE);

                    authors.add(author);
                    authorDao.saveAuthor(author);
                }
                else {
                    authors.add(optionalAuthor.get());
                }
            }
            return authors;
    }
}
