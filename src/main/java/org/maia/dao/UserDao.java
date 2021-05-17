package org.maia.dao;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.maia.domain.User;
import org.maia.mapper.UserMapper;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


@RegisterBeanMapper(UserMapper.class)
public interface UserDao extends Serializable {

    @SqlUpdate("INSERT INTO User_TB(identification,nome,email)" +
            " VALUES(:identification,:nome,:email);")
    @GetGeneratedKeys
    String insert(@BindBean User user);

    @SqlQuery("SELECT * FROM User_TB as u WHERE u.identification = :identification")
    User findByIdentification(@Bind("identification") String identification);

    @SqlQuery("SELECT * FROM User_TB")
    Set<User> findAll();

    @SqlUpdate("DELETE FROM User_TB WHERE identification = :identification")
    void delete(@Bind("identification") String identification);

    @SqlUpdate("UPDATE User_TB AS u SET u.email = :email WHERE u.identification = :identification")
    @GetGeneratedKeys
    String update(@Bind("email") String email, @Bind("identification") String identification);



}
