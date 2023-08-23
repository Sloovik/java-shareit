package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> getAll();

    Optional<User> findById(long userId);

    User create(User user);

    User update(User user);

    void delete(Long userId);

    void checkEmailExist(Long userId, String email);
}
