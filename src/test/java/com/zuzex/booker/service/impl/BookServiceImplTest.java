package com.zuzex.booker.service.impl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest
@ExtendWith(SpringExtension.class)
class BookServiceImplTest {

//    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";
//
//    @Autowired
//    private BookService bookService;
//
//    @MockBean
//    private BookRepository bookRepository;
//
//    @MockBean
//    private AuthorRepository authorRepository;
//
//    @MockBean
//    private RestTemplate restTemplate;
//
//    @MockBean
//    private UserService userService;
//
//    Book firstBook;
//    Book secondBook;
//    Author author;
//    BookResponse bookResponse;
//    Optional<Book> optionalFirstBook;
//    Optional<Author> optionalAuthor;
//
//    @BeforeEach
//    void setup() {
//
//        bookResponse = new BookResponse();
//        bookResponse.setTitle("Abba");
//        bookResponse.setDate("12.12.2020");
//        bookResponse.setAuthors(new ArrayList<>());
//        bookResponse.setToken("newToken");
//
//        firstBook = new Book();
//        firstBook.setId(1L);
//        firstBook.setTitle("Abba");
//        firstBook.setDate("12.12.2020");
//        firstBook.setIsRead(false);
//        firstBook.setAuthors(new ArrayList<>());
//        firstBook.setUsers(null);
//        firstBook.setCreated(new Date());
//        firstBook.setUpdated(new Date());
//        firstBook.setStatus(Status.ACTIVE);
//
//        secondBook = new Book();
//        secondBook.setId(2L);
//        secondBook.setTitle("Pudge");
//        secondBook.setDate("21.21.2012");
//        secondBook.setIsRead(true);
//        secondBook.setAuthors(new ArrayList<>());
//        secondBook.setUsers(null);
//        secondBook.setCreated(new Date());
//        secondBook.setUpdated(new Date());
//        secondBook.setStatus(Status.ACTIVE);
//
//        author = new Author();
//        author.setId(1L);
//        author.setName("Duma");
//        author.setBooks(List.of(firstBook,secondBook));
//        author.setCreated(new Date());
//        author.setUpdated(new Date());
//        author.setStatus(Status.ACTIVE);
//
//        optionalFirstBook = Optional.of(firstBook);
//        optionalAuthor = Optional.of(author);
//    }
//
//    @Test
//    void getBookById() {
//
//        Mockito.when(bookRepository.findById(1L)).thenReturn(ofNullable(firstBook));
//        Book resultBook = bookService.getBookById(1L);
//        Assertions.assertEquals(firstBook,resultBook);
//    }
//
//    @Test
//    void getBookByTitle() {
//        Mockito.when(bookRepository.findByTitle("Abba")).thenReturn(optionalFirstBook);
//        Book result = bookService.getBookByTitle("Abba");
//        Assertions.assertEquals(firstBook, result);
//    }
//
//    @Test
//    void getAllBooks() {
//        Mockito.when(bookRepository.findAll()).thenReturn(List.of(firstBook,secondBook));
//        List<Book> bookList  = bookService.getAllBooks();
//        Assertions.assertEquals(bookList.size(), 2);
//        Assertions.assertEquals(bookList.get(0), firstBook);
//        Assertions.assertEquals(bookList.get(1), secondBook);
//    }
//
//    @Test
//    void getAllReadBooks() {
//        Mockito.when(bookRepository.findByIsReadEquals(true)).thenReturn(List.of(secondBook));
//
//        List<Book> bookList = bookService.getAllReadBooks();
//
//        Assertions.assertNotEquals(firstBook,bookList.get(0));
//        Assertions.assertEquals(secondBook,bookList.get(0));
//        Assertions.assertEquals(bookList.size(), 1);
//    }
//
//    @Test
//    void getAllNoReadBooks() {
//        Mockito.when(bookRepository.findByIsReadEquals(false)).thenReturn(List.of(firstBook));
//
//        List<Book> bookList = bookService.getAllNoReadBooks();
//
//        Assertions.assertNotEquals(secondBook,bookList.get(0));
//        Assertions.assertEquals(firstBook,bookList.get(0));
//        Assertions.assertEquals(bookList.size(), 1);
//    }
//
//    @Test
//    void addToReadBooks() {
////        Mockito.when(bookService.addToReadBooks(firstBook)).thenReturn(true);
//
//        Assertions.assertTrue(bookService.addToReadBooks(firstBook));
//    }
//
//    @Test
//    void deleteFromReadBooks() {
//        Assertions.assertTrue(bookService.deleteFromReadBooks(secondBook));
//    }
//
//    @Test
//    void getBooksByDate() {
//
//        Mockito.when(bookRepository.findByDate("12.12.2020")).thenReturn(List.of(firstBook));
//
//        List<Book> bookList = bookService.getBooksByDate("12.12.2020");
//
//        Assertions.assertEquals(firstBook, bookList.get(0));
//        Assertions.assertEquals(firstBook.getTitle(), bookList.get(0).getTitle());
//        Assertions.assertTrue(bookList.size() == 1);
//    }
//
//    @Test
//    void createNewBookIfBookIsExist() {
//
//        Mockito.when(bookRepository.findByTitle(bookResponse.getTitle())).thenReturn(optionalFirstBook);
//
//        Book book = bookService.createNewBook(bookResponse);
//
//        Assertions.assertEquals(firstBook,book);
//        Assertions.assertEquals(firstBook.getTitle(), bookResponse.getTitle());
//        Assertions.assertEquals(bookResponse.getDate(), book.getDate());
//    }
//
//    @Test
//    void createNewBookIfBookDidNotExist() {
//
//        firstBook.setTitle("Abbat");
//        bookResponse.setTitle("Abbat");
//
//        Book book = bookService.createNewBook(bookResponse);
//
//        Assertions.assertEquals(firstBook, book);
//    }
//
//    @Test
//    void getBooksByAuthor() {
//
//        Mockito.when(authorRepository.findById(1L)).thenReturn(ofNullable(author));
//
//        List<Book> bookList = bookService.getBooksByAuthor(author);
//
//        Assertions.assertEquals(bookList.size(), 2);
//        Assertions.assertEquals(firstBook, bookList.get(0));
//        Assertions.assertEquals(secondBook,bookList.get(1));
//
//    }
//
//    @Test
//    void getAuthorByName() {
//
//        Mockito.when(authorRepository.findByName("Duma")).thenReturn(optionalAuthor);
//
//        Author result = bookService.getAuthorByName("Duma");
//
//        Assertions.assertEquals(author, result);
//        Assertions.assertEquals(author.getName(), result.getName());
//    }
//
//    @Test
//    void deleteBookById() {
//
//        Mockito.when(bookRepository.findById(1L)).thenReturn(ofNullable(firstBook));
//
//        bookService.deleteBookById(1L);
//
//        Assertions.assertEquals(firstBook.getStatus(),Status.DELETED);
//
//    }
//
//    @Test
//    void getAllAuthors() {
//        Mockito.when(authorRepository.findAll()).thenReturn(List.of(author));
//
//        List<Author> authorList = bookService.getAllAuthors();
//
//        Assertions.assertEquals(author, authorList.get(0));
//        Assertions.assertEquals(authorList.size(), 1);
//        Assertions.assertEquals(author.getName(), authorList.get(0).getName());
//    }
//
//    @Test
//    void getAuthorById() {
//        Mockito.when(authorRepository.findById(1L)).thenReturn(ofNullable(author));
//
//        Author result = bookService.getAuthorById(1L);
//
//        Assertions.assertEquals(author,result);
//        Assertions.assertEquals(author.getName(), result.getName());
//    }
}