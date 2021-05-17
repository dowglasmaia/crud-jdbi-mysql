package org.maia.service;

import lombok.extern.slf4j.Slf4j;
import org.maia.dao.UserDao;
import org.maia.domain.User;
import org.maia.domain.UserUpdateDto;
import org.maia.mapper.UserMapper;
import org.maia.utils.UUIDGeneration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserServices {

    @Autowired
    private UserDao userDao;

    @Transactional
    public String createUser(User user) {
        String identificationUnique = UUIDGeneration.generationUUID();
        user.setIdentification(identificationUnique);
        try {
            return userDao.insert(user);
        } catch (Exception error) {
            log.error("Error au tentar Salvar User");
            log.error(error.getMessage(), error.getCause());
            throw new RuntimeException(error.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public User getUserByIdentification(String identification){
        try {
            return userDao.findByIdentification(identification);
        } catch (Exception error) {
            log.error("Falha ao tentar Buscar User pelo identification "+identification);
            log.error(error.getMessage(), error.getCause());
            throw new RuntimeException("Falha ao tentar Buscar User pelo identification "+identification);
        }
    }

    @Transactional(readOnly = true)
    public Set<User> getAllUsers() {
        try {
            return userDao.findAll();
        } catch (Exception error) {
            log.error("Falha ao tentar Buscar Todos os Usuarios ");
            log.error(error.getMessage(), error.getCause());
            throw new RuntimeException("Falha ao tentar Buscar Todos os Usuarios ");
        }
    }

    @Transactional
    public void deleteUser(String identification) {
        log.info("Iniciando Busca de Usuario para Exclusão");
        getUserByIdentification(identification);
        log.info("Usuario de identification "+ identification +" localizado para Exclusão " );
        try {
            userDao.delete(identification);
        } catch (Exception error) {
            log.error("Error au tentar Deletar User");
            log.error(error.getMessage(), error.getCause());
            throw new RuntimeException(error.getMessage());
        }
    }

    @Transactional
    public User updateUser(UserUpdateDto dto, String identification) {
        User user = User.builder().build();
        if (identification != null || !identification.isEmpty()) {
            user = getUserByIdentification(identification);
        }
        try {
            log.info("ID passado na request "+  identification);
            userDao.update(dto.getEmail(),identification);
            user = getUserByIdentification(identification);
            return user;
        } catch (Exception error) {
            log.error(error.getMessage());
            throw new RuntimeException(error.getMessage());
        }
    }

}
