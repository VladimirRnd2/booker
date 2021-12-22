package com.zuzex.booker.mapper;

import com.zuzex.booker.dto.UserBookRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserBookMapper {

    void addBookToUser(UserBookRequest request);
}
