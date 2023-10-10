package ru.practicum.shareit.item.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInRequestDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ItemMapper {

    public static ItemDto toResponseDto(Item item, BookingItemDto lastBooking, BookingItemDto nextBooking,
                                        List<Comment> comments) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(UserMapper.toDto(item.getOwner()))
                .requestId(item.getRequestId())
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments != null
                        ? comments.stream().map(CommentMapper::toDto).collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }

    public static ItemDto toDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(UserMapper.toDto(item.getOwner()))
                .requestId(item.getRequestId())
                .comments(new ArrayList<>())
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(UserMapper.toUser(itemDto.getOwner()))
                .requestId(itemDto.getRequestId())
                .build();
    }

    public static ItemInRequestDto toItemInRequestDto(Item item) {
        return ItemInRequestDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequestId())
                .build();
    }

}