package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private Long lastId = 0L;

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findById(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public User create(User user) {
        lastId++;
        user.setId(lastId);

        users.put(lastId, user);

        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public void delete(Long userId) {
        users.remove(userId);
    }

    @Override
    public void checkEmailExist(Long userId, String email) {
        boolean emailExist = users.values().stream()
                .anyMatch(u -> {
                    if (userId == null) {
                        return Objects.equals(u.getEmail(), email);
                    }

                    return !Objects.equals(u.getId(), userId) && Objects.equals(u.getEmail(), email);
                });

        if (emailExist) {
            throw new AlreadyExistException(String.format("User email %s already exist!", email));
        }
    }

}
