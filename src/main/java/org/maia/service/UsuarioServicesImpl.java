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
@RegisterBeanMapper(UserMapper.class)
public class UsuarioServicesImpl implements UserRepository {

    @Autowired
    Jdbi jdbi;

    @Override
    public String insert(User user) {
        return jdbi.withHandle(handle -> {
            handle.createUpdate(
                    "INSERT INTO User_TB(identification, nome, email)" +
                            " VALUES(:identification, :nome, :email)")
                    .bindBean(User.builder()
                            .identification(user.getIdentification())
                            .nome(user.getNome())
                            .email(user.getEmail())
                            .build())
                    .execute();
            return null;
        });
    }


    /*@Override
    //@RegisterBeanMapper(UserMapper.class)
    public User findByIdentification(String identification) {
        return jdbi.withHandle(handle -> {
            handle.createQuery("SELECT * FROM User_TB as u " +
                    "WHERE u.identification = :identification")
                    .bind("identification", identification)
                    .mapToBean(User.class)
                    .one();
            return null;
        });
    }


    @Override
    public List<User> findAll() {
        List<User>users =   jdbi.withHandle(handle -> {
            handle.createQuery("SELECT * FROM User_TB ORDER BY nome")
                    .mapToBean(User.class)
                    .list();

            return null;
        });
        return users;
    }*/
}
