package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemInRequestDto;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ItemRequestMapperTest {

    private final List<ItemInRequestDto> items = new ArrayList<>();
    private ItemRequest itemRequest;
    private CreateItemRequestDto createItemRequestDto;

    @BeforeEach
    void init() {
        LocalDateTime created = LocalDateTime.now();

        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("test")
                .created(created)
                .requestor(new User(1L, "test", "test@email.ru"))
                .build();

        createItemRequestDto = CreateItemRequestDto.builder().description("test").build();
    }

    @Test
    void testToDto() {
        ItemRequestDto dto = ItemRequestMapper.toDto(itemRequest);

        assertNotNull(dto);
        assertEquals(dto.getId(), itemRequest.getId());
        assertEquals(dto.getDescription(), itemRequest.getDescription());
        assertEquals(dto.getCreated(), itemRequest.getCreated());
    }

    @Test
    void testCreateItemRequestToItemRequest() {
        ItemRequest model = ItemRequestMapper.createItemRequestToItemRequest(createItemRequestDto);

        assertNotNull(model);
        assertEquals(model.getDescription(), createItemRequestDto.getDescription());
    }

    @Test
    void testToItemRequestResponse() {
        ItemRequestResponseDto dto = ItemRequestMapper.toItemRequestResponse(itemRequest);

        assertNotNull(dto);
        assertEquals(dto.getId(), itemRequest.getId());
        assertEquals(dto.getDescription(), itemRequest.getDescription());
        assertEquals(dto.getCreated(), itemRequest.getCreated());
    }

    @Test
    void testToItemRequestWithItemsDto() {
        ItemRequestWithItemsDto dto = ItemRequestMapper.toItemRequestWithItemsDto(itemRequest, items);

        assertNotNull(dto);
        assertEquals(dto.getId(), itemRequest.getId());
        assertEquals(dto.getDescription(), itemRequest.getDescription());
        assertEquals(dto.getCreated(), itemRequest.getCreated());
        assertEquals(dto.getItems(), items);
    }

}
