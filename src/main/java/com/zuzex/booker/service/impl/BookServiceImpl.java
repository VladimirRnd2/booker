package com.zuzex.booker.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zuzex.booker.dto.*;
import com.zuzex.booker.dto.serializer.BookResponseDeserializer;
import com.zuzex.booker.model.Author;
import com.zuzex.booker.model.Book;
import com.zuzex.booker.model.Status;
import com.zuzex.booker.model.User;
import com.zuzex.booker.repository.dao.AuthorDao;
import com.zuzex.booker.repository.dao.BookAuthorDao;
import com.zuzex.booker.repository.dao.BookDao;
import com.zuzex.booker.repository.dao.UserBookDao;
import com.zuzex.booker.security.jwt.JwtFilter;
import com.zuzex.booker.security.jwt.JwtProv;
import com.zuzex.booker.service.BookService;
import com.zuzex.booker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final UserBookDao userBookDao;
    private final BookAuthorDao bookAuthorDao;
    private final RestTemplate restTemplate;
    private final UserService userService;
    private final JwtProv jwtProvider;
    private final JwtFilter jwtFilter;


    @Autowired
    public BookServiceImpl(BookDao bookDao, AuthorDao authorDao, UserBookDao userBookDao, BookAuthorDao bookAuthorDao, RestTemplate restTemplate, UserService userService, JwtFilter jwtFilter, JwtProv jwtProvider, JwtFilter jwtFilter1) {
        this.bookDao = bookDao;
        this.authorDao = authorDao;
        this.userBookDao = userBookDao;
        this.bookAuthorDao = bookAuthorDao;
        this.restTemplate = restTemplate;
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.jwtFilter = jwtFilter1;
    }

    @Override
    public Book getBookById(Long id) {
        Book book = bookDao.findBookById(id);
        if(book != null && book.getStatus() == Status.ACTIVE) {
            book.setAuthors(authorDao.getAuthorsByBookId(book.getId()));
            return book;
        }
        else
            throw new RuntimeException("Book with id " + id + " not found");
    }

    @Override
    public Book getBookByTitle(String title) {
        Book book = bookDao.findBookByTitle(title);
        if(book != null && book.getStatus() == Status.ACTIVE) {
            return book;
        }
        else
            throw new RuntimeException("Book with title " + title + " not found");
    }
// Здесь я должен делать на каждую сущность по отдельному потоку, используя findBookById(Long id)
    @Override
    public List<Book> getAllBooks() {
//        List<Book> bookList = bookDao.findAllBooks();
//        List<Book> resultList = new ArrayList<>();
//
//        for (Book book : bookList) {
//            if(book.getStatus() == Status.ACTIVE)
//                resultList.add(book);
//        }
//        return resultList;
        List<Book> books = new ArrayList<>();
        Long count = bookDao.getCountBooks();
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        try {
            executorService.submit(() -> {
                for (int id = 0; id < count ; id++) {
                    books.add(bookDao.findBookById(bookDao.getIdInBooks(id)));
                }
            }).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        executorService.shutdown();
        return books;
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
        Book book = bookDao.findBookByTitle(bookResponse.getTitle());

        if(book == null) {
            book = new Book();
            book.setTitle(bookResponse.getTitle());
            book.setDate(bookResponse.getDate());
            book.setAuthors(getAllAuthorsFromAuthorResponse(bookResponse));
            book.setIsRead(false);
            book.setCreated(new Date());
            book.setUpdated(new Date());
            book.setStatus(Status.ACTIVE);
            bookDao.saveBook(book);
            Long bookId = bookDao.findBookByTitle(bookResponse.getTitle()).getId();

            List<Author> authorList = new ArrayList<>();
            for (AuthorResponse authorResponse : bookResponse.getAuthors()){
                authorList.add(authorDao.findAuthorByName(authorResponse.getName()));
            }
            for(Author author : authorList) {
                bookAuthorDao.addAuthorToBook(new BookAuthorRequest(bookId,author.getId()));
            }
            User user = userService.findByLogin(jwtProvider.getLoginFromAccessToken(bookResponse.getToken()));
            userBookDao.addBookToUser(new UserBookRequest(user.getId(),bookId));

            return book;
//            userService.addBookToUser(book, bookResponse.getToken());
        } else {
            User user = userService.findByLogin(jwtProvider.getLoginFromAccessToken(bookResponse.getToken()));
            userBookDao.addBookToUser(new UserBookRequest(user.getId(), book.getId()));
            book.setAuthors(getAllAuthorsFromAuthorResponse(bookResponse));
//            book.setStatus(Status.ACTIVE);
//            bookDao.saveBook(book);
//            userService.addBookToUser(book, bookResponse.getToken());
            return book;
        }
    }

    @Override
    public List<Book> getBooksByAuthor(Author author) {
        author.setBooks(bookDao.getBooksByAuthorId(author.getId()));
        return author.getBooks();
    }

    @Override
    public Author getAuthorByName(String name) {
        Author optionalAuthor = authorDao.findAuthorByName(name);
        if(optionalAuthor != null)
            return optionalAuthor;
        else
            throw new RuntimeException("Author with this name not found");
    }

    @Override
    public void deleteBookById(Long id) {
//        Book book = bookRepository.findById(id).orElse(null);
//        book.setStatus(Status.DELETED); // будет NPE , устранить
//        book.setUpdated(new Date());
//        bookRepository.save(book);

        Book book = bookDao.findBookById(id);
        if(book != null) {
            book.setStatus(Status.DELETED);
            book.setUpdated(new Date());
            bookDao.saveBook(book);
        }
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorDao.findAllAuthors();
    }

    @Override
    public Author getAuthorById(Long id) {
        return authorDao.findAuthorById(id);
    }

    private List<Author> getAllAuthorsFromAuthorResponse(BookResponse bookResponse) {
            List<Author> authors = new ArrayList<>();
            Author author;
            for (AuthorResponse response : bookResponse.getAuthors()) {
                author = authorDao.findAuthorByName(response.getName());
                if(author == null) {
                    author = new Author();
                    author.setName(response.getName());
                    author.setCreated(new Date());
                    author.setUpdated(new Date());
                    author.setBooks(new ArrayList<>());
                    author.setStatus(Status.ACTIVE);

                    authors.add(author);
                    authorDao.saveAuthor(author);
                }
                else {
                    authors.add(author);
                }
            }
            return authors;
    }
}
