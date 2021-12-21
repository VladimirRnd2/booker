package com.zuzex.booker.mapper;

import com.zuzex.booker.model.RefreshToken;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface RefreshTokenMapper {

    RefreshToken findRefreshTokenByToken(@Param("token") String token);
    void saveRefreshToken(RefreshToken refreshToken);
    void deleteRefreshTokenByLogin(@Param("login") String login);

}
