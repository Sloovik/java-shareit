package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserClient userClient;

    @GetMapping()
    public ResponseEntity<Object> getAll() {
        return userClient.getAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findById(@PathVariable Long userId) {
        return userClient.findById(userId);
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto user) {
        return userClient.create(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable Long userId, @RequestBody UserDto user) {
        return userClient.update(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        userClient.delete(userId);
    }

}
