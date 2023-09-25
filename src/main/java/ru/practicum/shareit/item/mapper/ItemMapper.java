package ru.practicum.shareit.item.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ItemMapper {

    private final UserMapper userMapper;
    private final CommentMapper commentMapper;
    private final ItemRequestMapper itemRequestMapper;

    public ItemDto toResponseDto(Item item, BookingItemDto lastBooking, BookingItemDto nextBooking,
                                 List<Comment> comments) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(userMapper.toDto(item.getOwner()))
                .request(item.getRequest() != null ? itemRequestMapper.toDto(item.getRequest()) : null)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments != null
                        ? comments.stream().map(commentMapper::toDto).collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }

    public ItemDto toDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(userMapper.toDto(item.getOwner()))
                .request(item.getRequest() != null ? itemRequestMapper.toDto(item.getRequest()) : null)
                .comments(new ArrayList<>())
                .build();
    }

    public Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(userMapper.toUser(itemDto.getOwner()))
                .request(itemDto.getRequest() != null ? itemRequestMapper.toItemRequest(itemDto.getRequest()) : null)
                .build();
    }

}