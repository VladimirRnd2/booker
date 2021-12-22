package com.zuzex.booker.repository.dao;

import com.zuzex.booker.model.Role;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDao {

    private final SqlSession sqlSession;

    @Autowired
    public RoleDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public Role findRoleById(Long id) {
        return this.sqlSession.selectOne("findRoleById", id);
    }

    public Role findRoleByName(String name) {
        return this.sqlSession.selectOne("findRoleByName", name);
    }

    public Role findRoleByUserId(Long id) {
        return this.sqlSession.selectOne("findRoleByUserId", id);
    }
}
