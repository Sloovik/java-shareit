package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ItemDto {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private UserDto owner;

    private ItemRequestDto request;

    private Long requestId;

    private BookingItemDto lastBooking;

    private BookingItemDto nextBooking;

    private List<CommentDto> comments;
}
