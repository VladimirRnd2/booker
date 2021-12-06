package com.zuzex.booker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "books")
@Data
public class Book extends BaseEntity{

    @Column(name = "title")
    private String title;


    @Column(name = "date")
    private String date;

    @Column(name = "is_read")
    private Boolean isRead;

    @ManyToMany(mappedBy = "books" , fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> users;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "books_authors",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id"))
    private List<Author> authors;


}
