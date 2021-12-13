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
import com.zuzex.booker.repository.AuthorRepository;
import com.zuzex.booker.repository.BookRepository;
import com.zuzex.booker.service.BookService;
import com.zuzex.booker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private RestTemplate restTemplate;
    private UserService userService;


    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, RestTemplate restTemplate, UserService userService) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.restTemplate = restTemplate;
        this.userService = userService;
    }

    @Override
    public Book getBookById(Long id) {
//        Book book = bookRepository.findById(id).orElse(null);
//        if(book.getStatus() == Status.ACTIVE)
//            return book;
//        else
//            return null;

        Optional<Book> book = bookRepository.findById(id);
        if(book.isPresent() && book.get().getStatus() == Status.ACTIVE) {
            return book.get();
        }
        else
            throw new RuntimeException("Book with id " + id + " not found");
    }

    @Override
    public Book getBookByTitle(String title) {
        Book book = bookRepository.findByTitle(title);
        if(book.getStatus() == Status.ACTIVE)
            return book;
        else
            return null;
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> bookList = bookRepository.findAll();
        List<Book> resultList = new ArrayList<>();

        for (Book book : bookList) {
            if(book.getStatus() == Status.ACTIVE)
                resultList.add(book);
        }
        return resultList;
    }

    @Override
    public List<Book> getAllReadBooks() {
        List<Book> bookList = bookRepository.findByIsReadEquals(true);
        List<Book> resultList = new ArrayList<>();

        for (Book book : bookList) {
            if(book.getStatus() == Status.ACTIVE)
                resultList.add(book);
        }
        return resultList;
    }

    @Override
    public List<Book> getAllNoReadBooks() {
        List<Book> bookList = bookRepository.findByIsReadEquals(false);
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
        bookRepository.save(book);
        return true;
    }

    @Override
    public boolean deleteFromReadBooks(Book book) {
        if(!book.getIsRead())
            return false;
        book.setIsRead(false);
        bookRepository.save(book);
        return true;
    }

    @Override
    public List<Book> getBooksByDate(String date) {
        List<Book> bookList = bookRepository.findByDate(date);
        List<Book> resultList = new ArrayList<>();

        for (Book book : bookList) {
            if(book.getStatus() == Status.ACTIVE)
                resultList.add(book);
        }
        return resultList;
    }

    @Override
    public BookResponse getBookResponse(BookRequest bookRequest) {
        String responseEntity = restTemplate.getForObject(BASE_URL.concat(bookRequest.getTitle()),String.class);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(BookResponse.class, new BookResponseDeserializer())
                .create();
        BookResponse bookResponse = gson.fromJson(responseEntity,BookResponse.class);
        return bookResponse;
    }

    @Override
    public Book createNewBook(BookResponse bookResponse) {
        Book book = null;
        if(!isExistByTitle(bookResponse.getTitle())) {
            book = new Book();
            book.setTitle(bookResponse.getTitle());
            book.setDate(bookResponse.getDate());
            book.setAuthors(getAllAuthorsFromAuthorResponse(bookResponse));
            book.setIsRead(false);
            book.setCreated(new Date());
            book.setUpdated(new Date());
            book.setStatus(Status.ACTIVE);

            bookRepository.save(book);

            userService.addBookToUser(book, bookResponse.getToken());

            return book;

        }
        book = bookRepository.findByTitle(bookResponse.getTitle());
        book.setStatus(Status.ACTIVE);
        bookRepository.save(book);
        userService.addBookToUser(book,bookResponse.getToken());
        return book;
    }

    @Override
    public List<Book> getBooksByAuthor(Author author) {
        return author.getBooks();
    }

    @Override
    public Author getAuthorByName(String name) {
        return authorRepository.findByName(name);
    }

    @Override
    public void deleteBookById(Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        book.setStatus(Status.DELETED); // будет NPE , устранить
        book.setUpdated(new Date());
        bookRepository.save(book);
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id).orElse(null);
    }

    private List<Author> getAllAuthorsFromAuthorResponse(BookResponse bookResponse) {
            List<Author> authors = new ArrayList<>();
            Author author = null;
            for (AuthorResponse response : bookResponse.getAuthors()) {
                if(authorRepository.findByName(response.getName())== null) {
                    author = new Author();
                    author.setName(response.getName());
                    author.setCreated(new Date());
                    author.setUpdated(new Date());
                    author.setStatus(Status.ACTIVE);

                    authors.add(author);
                    authorRepository.save(author);
                }
                else {
                    author = authorRepository.findByName(response.getName());
                    authors.add(author);
                }

            }
            return authors;
    }

    private boolean isExistByTitle(String title) {
        if(bookRepository.findByTitle(title) == null) {
            return false;
        }
        return true;
    }

    private boolean isExistAuthors(String name) {
        if(authorRepository.findByName(name) == null) {
            return false;
        }
        return true;
    }
}
