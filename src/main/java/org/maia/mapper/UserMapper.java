package org.maia.mapper;

import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.maia.domain.User;
import org.maia.utils.UUIDGeneration;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Component
public class UserMapper implements RowMapper<User> {

    @Override
    public User map(ResultSet rs, StatementContext ctx) throws SQLException {
        User user = User.builder()
                .identification(rs.getString("identification"))
                .nome(rs.getString("nome"))
                .email(rs.getString("email"))
                .build();
        log.info("UserMapper :> " +user);
        return user;

    }
}
