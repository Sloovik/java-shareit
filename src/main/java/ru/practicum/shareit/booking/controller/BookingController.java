package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final BookingService bookingService;

    @GetMapping
    public List<BookingDto> getAllBookingsForUser(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                                  @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.getAllBookingsForUser(userId, state);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader(name = USER_ID_HEADER) Long userId, @PathVariable Long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllItemsBookingForUser(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                                      @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllItemsBookingForUser(userId, state);
    }

    @PostMapping
    public BookingDto create(@RequestHeader(name = USER_ID_HEADER) Long userId,
                             @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        return bookingService.create(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateStatus(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                   @PathVariable Long bookingId, @RequestParam Boolean approved) {
        return bookingService.updateStatus(userId, bookingId, approved);
    }

}
