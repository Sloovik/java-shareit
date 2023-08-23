package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAll() {
        return userRepository.getAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %x not found!", userId)));

        return userMapper.toDto(user);
    }

    @Override
    public UserDto create(UserDto userDto) {
        userRepository.checkEmailExist(userDto.getId(), userDto.getEmail());
        return userMapper.toDto(userRepository.create(userMapper.toUser(userDto)));
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %x not found!", userId)));

        if (userDto.getEmail() != null) {
            userRepository.checkEmailExist(user.getId(), userDto.getEmail());
            user.setEmail(userDto.getEmail());
        }

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        return userMapper.toDto(userRepository.update(user));
    }

    @Override
    public void delete(Long userId) {
        userRepository.delete(userId);
    }

}
