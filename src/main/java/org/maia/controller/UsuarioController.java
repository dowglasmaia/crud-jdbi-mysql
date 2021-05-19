package org.maia.controller;

import org.maia.domain.User;
import org.maia.service.UserServices02;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v2/usuarios")
public class UsuarioController {


    @Autowired
    private UserServices02 services;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user) {
        String identificationUser = services.createUser(user);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{identification}")
                .buildAndExpand(user.getIdentification()).toUri();
        return ResponseEntity.created(uri).build();
    }

/*
    @GetMapping("/{identification}")
    public ResponseEntity<User> getUserByIdentification(@PathVariable String identification) {

        User user = services.getUserByIdentification(identification);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        List<User> users = services.getAll();
        return ResponseEntity.ok().body(users);
    }*/

}
