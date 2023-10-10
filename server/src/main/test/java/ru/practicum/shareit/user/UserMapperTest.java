package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;


import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserMapperTest {

    private User user;
    private UserDto userDto;

    @BeforeEach
    void init() {
        user = User.builder()
                .id(1L)
                .name("Name")
                .email("some@email.ru")
                .build();
        userDto = UserDto.builder()
                .id(1L)
                .name("Name")
                .email("some@email.ru")
                .build();
    }

    @Test
    void testToUser() {
        User toUser = UserMapper.toUser(userDto);

        assertEquals(toUser.getId(), user.getId());
        assertEquals(toUser.getName(), user.getName());
        assertEquals(toUser.getEmail(), user.getEmail());
    }

    @Test
    void testToDto() {
        UserDto toUserDto = UserMapper.toDto(user);

        assertEquals(toUserDto.getId(), userDto.getId());
        assertEquals(toUserDto.getName(), userDto.getName());
        assertEquals(toUserDto.getEmail(), userDto.getEmail());
    }

}
