package com.zuzex.booker.mapper;

import com.zuzex.booker.model.Author;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuthorMapper {

    List<Author> findAllAuthors();
    List<Author> getAuthorsByBookId(@Param("id") Long id);
    Author findAuthorById(@Param("id") Long id);
    Author findAuthorByName(@Param("name") String name);
    Long saveAuthor(Author author);


}
