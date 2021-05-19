package org.maia.dao;

import org.maia.domain.User;

import java.io.Serializable;
import java.util.List;


public interface UserRepository extends Serializable {

    String insert(User user);

    /*User findByIdentification(String identification);

    List<User>findAll();*/


}
