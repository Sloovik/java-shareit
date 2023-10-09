package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ItemMapperTest {

    private Item item;
    private ItemDto itemDto;
    private List<Comment> comments;
    private BookingItemDto bookingItemDto;

    @BeforeEach
    void init() {
        User user = User.builder().build();
        UserDto userDto = UserDto.builder().build();

        item = Item.builder()
                .id(1L)
                .name("test")
                .description("test")
                .available(true)
                .owner(user)
                .requestId(1L)
                .build();

        comments = List.of(Comment.builder()
                .id(1L)
                .text("test")
                .item(item)
                .author(user)
                .created(LocalDateTime.now())
                .build());

        bookingItemDto = BookingItemDto.builder().build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("test")
                .description("test")
                .available(true)
                .owner(userDto)
                .lastBooking(bookingItemDto)
                .nextBooking(bookingItemDto)
                .comments(new ArrayList<>())
                .requestId(1L)
                .build();
    }

    @Test
    void testToResponseDto() {
        ItemDto dto = ItemMapper.toResponseDto(item, bookingItemDto, bookingItemDto, comments);

        assertNotNull(dto);
        assertEquals(dto.getId(), item.getId());
        assertEquals(dto.getName(), item.getName());
        assertEquals(dto.getDescription(), item.getDescription());
        assertEquals(dto.getAvailable(), item.getAvailable());
        assertEquals(dto.getComments().size(), comments.size());
        assertEquals(dto.getRequestId(), item.getRequestId());

        ItemDto dto1 = ItemMapper.toResponseDto(item, bookingItemDto, bookingItemDto, null);

        assertEquals(dto1.getComments().size(), 0);
    }

    @Test
    void testToDto() {
        ItemDto dto = ItemMapper.toDto(item);

        assertNotNull(dto);
        assertEquals(dto.getId(), item.getId());
        assertEquals(dto.getName(), item.getName());
        assertEquals(dto.getDescription(), item.getDescription());
        assertEquals(dto.getAvailable(), item.getAvailable());
        assertEquals(dto.getRequestId(), item.getRequestId());
    }

    @Test
    void testToItem() {
        Item model = ItemMapper.toItem(itemDto);

        assertNotNull(model);
        assertEquals(model.getId(), itemDto.getId());
        assertEquals(model.getName(), itemDto.getName());
        assertEquals(model.getDescription(), itemDto.getDescription());
        assertEquals(model.getAvailable(), itemDto.getAvailable());
        assertEquals(model.getRequestId(), itemDto.getRequestId());
    }

    @Test
    void testToItemInRequestDto() {
        ItemInRequestDto dto = ItemMapper.toItemInRequestDto(item);

        assertNotNull(dto);
        assertEquals(dto.getId(), item.getId());
        assertEquals(dto.getName(), item.getName());
        assertEquals(dto.getDescription(), item.getDescription());
        assertEquals(dto.getAvailable(), item.getAvailable());
        assertEquals(dto.getRequestId(), item.getRequestId());
    }

}
