package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.List;

public interface BookingService {

    List<BookingDto> getAllBookingsForUser(Long userId, String state);

    BookingDto getBooking(Long userId, Long bookingId);

    List<BookingDto> getAllItemsBookingForUser(Long userId, String state);

    BookingDto create(Long userId, BookingRequestDto bookingRequestDto);

    BookingDto updateStatus(Long userId, Long bookingId, Boolean approved);

}
