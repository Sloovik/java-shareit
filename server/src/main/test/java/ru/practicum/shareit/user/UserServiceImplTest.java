package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceImplTest {

    private final EntityManager em;
    private final UserService userService;

    @Test
    void testGetAll() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("1")
                .email("1@email.ru")
                .build();
        UserDto userDto1 = UserDto.builder()
                .id(2L)
                .name("2")
                .email("2@email.ru")
                .build();
        UserDto userDto2 = UserDto.builder()
                .id(3L)
                .name("3")
                .email("3@email.ru")
                .build();
        UserDto userDto3 = UserDto.builder()
                .id(4L)
                .name("4")
                .email("4@email.ru")
                .build();

        userService.create(userDto);
        userService.create(userDto1);
        userService.create(userDto2);
        userService.create(userDto3);

        List<UserDto> users = userService.getAll();

        assertEquals(4, users.size());
        assertEquals(userDto, users.get(0));
        assertEquals(userDto1, users.get(1));
        assertEquals(userDto2, users.get(2));
        assertEquals(userDto3, users.get(3));
    }

    @Test
    void testFindById() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("1")
                .email("1@email.ru")
                .build();
        UserDto userDto1 = UserDto.builder()
                .id(2L)
                .name("2")
                .email("2@email.ru")
                .build();

        userService.create(userDto);
        userService.create(userDto1);

        assertEquals(1L, userService.findById(1L).getId());
        assertEquals(2L, userService.findById(2L).getId());
    }

    @Test
    void testFindByIdError() {
        Long notFoundUserId = 10L;

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                userService.findById(notFoundUserId));

        assertThat(exception.getMessage(), equalTo(String.format("User with id %x not found!", notFoundUserId)));
    }

    @Test
    void testCreate() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("1")
                .email("1@email.ru")
                .build();

        userService.create(userDto);

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);

        User user = query.setParameter("email", userDto.getEmail()).getSingleResult();

        assertThat(user.getId(), equalTo(1L));
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void testUpdate() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("1")
                .email("1@email.ru")
                .build();

        userService.create(userDto);

        userDto.setName("2");
        userDto.setEmail("2@email.ru");

        userService.update(1L, userDto);

        assertThat("2", equalTo(userDto.getName()));
        assertThat("2@email.ru", equalTo(userDto.getEmail()));
    }

    @Test
    void testUpdateError() {
        Long notFoundUserId = 10L;
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("1")
                .email("1@email.ru")
                .build();

        userService.create(userDto);

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                userService.update(notFoundUserId, userDto));

        assertThat(exception.getMessage(), equalTo(String.format("User with id %x not found!", notFoundUserId)));
    }

    @Test
    void testDelete() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("1")
                .email("1@email.ru")
                .build();
        UserDto userDto1 = UserDto.builder()
                .id(2L)
                .name("2")
                .email("2@email.ru")
                .build();

        userService.create(userDto);
        userService.create(userDto1);
        userService.delete(1L);

        assertThat(1, equalTo(userService.getAll().size()));
    }

    @Test
    void testDeleteError() {
        Long notFoundUserId = 10L;

        EmptyResultDataAccessException exception = assertThrows(EmptyResultDataAccessException.class, () ->
                userService.delete(notFoundUserId));

        assertThat(exception.getMessage(), equalTo("No class ru.practicum.shareit.user.model.User entity with id 10 exists!"));
    }

}
