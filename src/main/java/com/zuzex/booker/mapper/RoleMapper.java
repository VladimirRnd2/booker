package com.zuzex.booker.mapper;

import com.zuzex.booker.model.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RoleMapper {

    Role findRoleById(@Param("id") Long id);
    Role findRoleByName(@Param("name") String name);
    void saveRole(Role role);
}
