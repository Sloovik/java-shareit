package ru.practicum.shareit.booking.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

@Component
@AllArgsConstructor
public class BookingMapper {

    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    public BookingDto toDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStartDate())
                .end(booking.getEndDate())
                .item(booking.getItem() != null ? itemMapper.toDto(booking.getItem()) : null)
                .booker(userMapper.toDto(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }

    public BookingItemDto toBookingItemDto(Booking booking) {
        return BookingItemDto.builder()
                .id(booking.getId())
                .start(booking.getStartDate())
                .end(booking.getEndDate())
                .bookerId(booking.getBooker().getId())
                .build();
    }

    public Booking toBookingFromRequest(BookingRequestDto bookingRequestDto, Item item, User booker) {
        return Booking.builder()
                .id(bookingRequestDto.getId())
                .startDate(bookingRequestDto.getStart())
                .endDate(bookingRequestDto.getEnd())
                .item(item)
                .booker(booker)
                .status(Status.WAITING)
                .build();
    }

}
