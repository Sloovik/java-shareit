package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private User user;
    private UserDto userDto;
    private UserDto userDto1;

    @BeforeEach
    void init() {
        user = User.builder()
                .id(1L)
                .name("Name")
                .email("some@email.ru")
                .build();

        userDto = UserMapper.toDto(user);
        userDto1 = UserMapper.toDto(User.builder()
                .id(2L)
                .name("NewName")
                .email("new@email.ru")
                .build());
    }

    @Test
    void testGetAll() throws Exception {
        Mockito
                .when(userService.getAll()).thenReturn(List.of(userDto, userDto1));

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(userDto.getName())))
                .andExpect(jsonPath("$[0].email", is(userDto.getEmail())))
                .andExpect(jsonPath("$[1].name", is(userDto1.getName())))
                .andExpect(jsonPath("$[1].email", is(userDto1.getEmail())));

        Mockito.verify(userService).getAll();
    }

    @Test
    void testFindById() throws Exception {
        Mockito
                .when(userService.findById(Mockito.any())).thenReturn(userDto);

        mvc.perform(get("/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        Mockito.verify(userService).findById(user.getId());
    }

    @Test
    void testCreate() throws Exception {
        Mockito
                .when(userService.create(Mockito.any())).thenReturn(userDto);

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        Mockito.verify(userService).create(userDto);
    }

    @Test
    void testUpdate() throws Exception {
        userDto.setName("John");

        Mockito
                .when(userService.update(Mockito.any(), Mockito.any())).thenReturn(userDto);

        mvc.perform(patch("/users/{id}", 1L)
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        Mockito.verify(userService).update(1L, userDto);
    }

    @Test
    void testDelete() throws Exception {
        mvc.perform(delete("/users/{id}", userDto.getId()))
                .andExpect(status().isOk());

        Mockito.verify(userService).delete(1L);
    }

}
