package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @GetMapping()
    public List<UserDto> getAll() {
        log.info("GET request to get all users");
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    public UserDto findById(@PathVariable Long userId) {
        log.info("GET request to find user by user id: {}", userId);
        return userService.findById(userId);
    }

    @PostMapping()
    public UserDto create(@RequestBody UserDto user) {
        log.info("POST request to create user: {}", user);
        return userService.create(user);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Long userId, @RequestBody UserDto user) {
        log.info("PATCH request to update user by user id: {} user: {}", userId, user);
        return userService.update(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("DELETE request to delete user by user id: {}", userId);
        userService.delete(userId);
    }

}
