package org.maia.service;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.maia.dao.UserRepository;
import org.maia.domain.User;
import org.maia.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


//referencia  https://jdbi.org/
//https://medium.com/cwi-software/jdbi-uma-alternativa-al%C3%A9m-de-jdbc-e-jpa-hibernate-fb9ce29fdc

@Component

public class UsuarioServicesImpl implements UserRepository {

    @Autowired
    Jdbi jdbi;

    @Override
    @RegisterBeanMapper(UserMapper.class)
    public User insert(User user) {
       return jdbi.withHandle(handle -> {
            handle.createUpdate(
                    "INSERT INTO User_TB(identification, nome, email)" +
                            " VALUES(?, ?, ?);")
                    .bind(0,user.getIdentification())
                    .bind(1,user.getNome())
                    .bind(2,user.getEmail())
                    .execute();
           return null;
        });

    }


}
