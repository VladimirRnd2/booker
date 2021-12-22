package com.zuzex.booker.mapper;

import com.zuzex.booker.dto.BookAuthorRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookAuthorMapper {

    void addAuthorToBook(BookAuthorRequest request);
}
