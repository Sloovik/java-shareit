package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.utils.HttpHeaders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc
public class ItemControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private ItemDto itemDto;
    private CommentDto commentDto;

    private final int from = 0;
    private final int size = 10;
    private final Pageable pageRequest = PageRequest.of(from, size, Sort.by("id"));

    @BeforeEach
    void init() {
        itemDto = ItemDto.builder()
                .id(1L)
                .name("test")
                .description("test")
                .available(true)
                .build();

        commentDto = CommentDto.builder()
                .id(1L)
                .text("test")
                .build();
    }

    @Test
    void testFindByUserId() throws Exception {
        Mockito
                .when(itemService.findByUserId(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .thenReturn(new ArrayList<>());

        mvc.perform(get("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.USER_ID_HEADER, 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andDo(print());

        Mockito.verify(itemService).findByUserId(1L, pageRequest);
    }


    @Test
    void testFindById() throws Exception {
        Mockito
                .when(itemService.findById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemDto);

        mvc.perform(get("/items/{id}", itemDto.getId())
                        .content(objectMapper.writeValueAsBytes(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.USER_ID_HEADER, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        Mockito.verify(itemService).findById(1L, itemDto.getId());
    }

    @Test
    void testSearch() throws Exception {
        String text = "test";

        Mockito
                .when(itemService.search(Mockito.anyString(), Mockito.any(Pageable.class)))
                .thenReturn(new ArrayList<>());

        mvc.perform(get("/items/search?text=test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.USER_ID_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andDo(print());

        Mockito.verify(itemService).search(text, pageRequest);
    }

    @Test
    void testCreate() throws Exception {
        Mockito
                .when(itemService.create(Mockito.any(), Mockito.any())).thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.USER_ID_HEADER, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        Mockito.verify(itemService).create(1L, itemDto);
    }

    @Test
    void testUpdate() throws Exception {
        itemDto.setName("new");

        Mockito
                .when(itemService.update(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenReturn(itemDto);

        mvc.perform(patch("/items/{id}", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.USER_ID_HEADER, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void testCreateComment() throws Exception {
        Mockito
                .when(itemService.createComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/{itemId}/comment", 1L)
                        .content(objectMapper.writeValueAsString(CommentRequestDto.builder()
                                .text("new")
                                .build()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.USER_ID_HEADER, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

}
