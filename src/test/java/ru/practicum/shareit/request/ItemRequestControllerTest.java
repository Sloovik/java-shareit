package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.utils.HttpHeaders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@AutoConfigureMockMvc
public class ItemRequestControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService itemRequestService;

    private CreateItemRequestDto createItemRequestDto;
    private ItemRequestResponseDto itemRequestResponseDto;
    private ItemRequestWithItemsDto itemRequestWithItemsDto;

    private final int from = 0;
    private final int size = 20;
    private final Pageable pageRequest = PageRequest.of(from, size, Sort.by("created").descending());

    @BeforeEach
    void init() {
        createItemRequestDto = CreateItemRequestDto.builder()
                .description("test")
                .build();

        itemRequestResponseDto = ItemRequestResponseDto.builder()
                .id(1L)
                .description("test")
                .created(LocalDateTime.now())
                .build();

        itemRequestWithItemsDto = ItemRequestWithItemsDto.builder()
                .id(1L)
                .description("test")
                .created(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();
    }

    @Test
    void testFindAllByUserId() throws Exception {
        Mockito
                .when(itemRequestService.findAllByUserId(Mockito.anyLong())).thenReturn(new ArrayList<>());

        mvc.perform((get("/requests"))
                        .header(HttpHeaders.USER_ID_HEADER, "1"))
                .andExpect(status().isOk())
                .andDo(print());

        Mockito.verify(itemRequestService).findAllByUserId(1L);
    }

    @Test
    void testFindAll() throws Exception {
        Mockito
                .when(itemRequestService.findAll(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .thenReturn(new ArrayList<>());

        mvc.perform(get("/requests/all")
                        .header(HttpHeaders.USER_ID_HEADER, "1")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andDo(print());

        Mockito.verify(itemRequestService).findAll(1L, pageRequest);
    }

    @Test
    void testFindById() throws Exception {
        Mockito
                .when(itemRequestService.findById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemRequestWithItemsDto);

        mvc.perform(get("/requests/{requestId}", 1L)
                        .header(HttpHeaders.USER_ID_HEADER, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestWithItemsDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestWithItemsDto.getDescription())));

        Mockito.verify(itemRequestService).findById(1L, 1L);
    }

    @Test
    void testCreate() throws Exception {
        Mockito
                .when(itemRequestService.create(Mockito.anyLong(), Mockito.any()))
                .thenReturn(itemRequestResponseDto);

        mvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(createItemRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.USER_ID_HEADER, "1")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestResponseDto.getDescription())));

        Mockito.verify(itemRequestService).create(1L, createItemRequestDto);
    }

}
