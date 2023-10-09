package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.utils.HttpHeaders;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public List<BookingDto> getAllBookingsForUser(@RequestHeader(name = HttpHeaders.USER_ID_HEADER) Long userId,
                                                  @RequestParam(defaultValue = "ALL") State state,
                                                  @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("startDate").descending());

        return bookingService.getAllBookingsForUser(userId, state, pageable);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader(name = HttpHeaders.USER_ID_HEADER) Long userId,
                                 @PathVariable Long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllItemsBookingForUser(@RequestHeader(name = HttpHeaders.USER_ID_HEADER) Long userId,
                                                      @RequestParam(defaultValue = "ALL") State state,
                                                      @RequestParam(defaultValue = "0") Integer from,
                                                      @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("startDate").descending());

        return bookingService.getAllItemsBookingForUser(userId, state, pageable);
    }

    @PostMapping
    public BookingDto create(@RequestHeader(name = HttpHeaders.USER_ID_HEADER) Long userId,
                             @RequestBody BookingRequestDto bookingRequestDto) {
        return bookingService.create(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateStatus(@RequestHeader(name = HttpHeaders.USER_ID_HEADER) Long userId,
                                   @PathVariable Long bookingId, @RequestParam Boolean approved) {
        return bookingService.updateStatus(userId, bookingId, approved);
    }

}

