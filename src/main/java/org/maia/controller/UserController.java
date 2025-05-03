package org.maia.controller;

import org.maia.domain.User;
import org.maia.enums.EventType;
import org.maia.model.UserUpdateDto;
import org.maia.service.UserPublisher;
import org.maia.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {

    @Autowired
    private UserServices services;

    @Autowired
    private UserPublisher userPublisher;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user) {
        String identificationUser = services.createUser(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{identification}")
                .buildAndExpand(user.getIdentification()).toUri();


        userPublisher.publishUserEvent(user, EventType.USER_CREATED,"dowglas.maia");

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{identification}")
    public ResponseEntity<User> UpdateUser(@PathVariable String identification ,
                                           @RequestBody UserUpdateDto dto) {
        User userUpdated = services.updateUser(dto, identification);

        userPublisher.publishUserEvent(userUpdated, EventType.USER_UPDATED,"dowglas.maia");
        return ResponseEntity.ok().body(userUpdated);
    }

    @GetMapping("/{identification}")
    public ResponseEntity<User> findUserByIdentification(@PathVariable String identification) {

        User user = services.getUserByIdentification(identification);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping
    public ResponseEntity<Set<User>> findAllUser() {
        Set<User> users = services.getAllUsers();
        return ResponseEntity.ok().body(users);
    }

    @DeleteMapping("/{identification}")
    public ResponseEntity<?> deleteUser(@PathVariable String identification) {
        User user = services.getUserByIdentification(identification);

        services.deleteUser(user.getIdentification());

        userPublisher.publishUserEvent(user, EventType.USER_DELETED,"kayron.maia");
        return ResponseEntity.noContent().build();
    }


}
