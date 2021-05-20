package org.maia.service;

import lombok.extern.slf4j.Slf4j;
import org.maia.dao.UserDao;
import org.maia.dao.UserRepository;
import org.maia.domain.User;
import org.maia.domain.UserUpdateDto;
import org.maia.utils.UUIDGeneration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
public class UserServices02 {


    private UserRepository userRepository;

    @Autowired
    public UserServices02(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(User user) {
        String identificationUnique = UUIDGeneration.generationUUID();
        user.setIdentification(identificationUnique);
        try {
            log.info("Salvando Usuario com Services v2");
            return userRepository.insert(user);
        } catch (Exception error) {
            log.error("Error au tentar Salvar User");
            log.error(error.getMessage(), error.getCause());
            throw new RuntimeException(error.getMessage());
        }
    }


}
