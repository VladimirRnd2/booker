package com.zuzex.booker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;


@Data
public class Book extends BaseEntity{


    private String title;
    private String date;
    private Boolean isRead;
    @JsonIgnore
    private List<User> users;
    private List<Author> authors;


}
