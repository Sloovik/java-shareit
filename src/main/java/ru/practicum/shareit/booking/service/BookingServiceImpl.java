package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingMapper bookingMapper;

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Override
    public List<BookingDto> getAllBookingsForUser(Long userId, String state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %x not found!", userId)));

        List<Booking> bookings = new ArrayList<>();

        State stateVal = parseState(state);

        switch (stateVal) {
            case ALL:
                bookings = bookingRepository.findAllForBooker(userId);
                break;

            case PAST:
                bookings = bookingRepository.findPastBookingsForBooker(userId, LocalDateTime.now());
                break;

            case FUTURE:
                bookings = bookingRepository.findFutureBookingsForBooker(userId, LocalDateTime.now());
                break;

            case CURRENT:
                bookings = bookingRepository.findCurrentBookingsForBooker(userId, LocalDateTime.now());
                break;

            case WAITING:
                bookings = bookingRepository.findWaitingBookingsForBooker(userId);
                break;

            case REJECTED:
                bookings = bookingRepository.findRejectedBookingsForBooker(userId);
        }

        return bookings.stream().map(bookingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public BookingDto getBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Booking with id %x not found!", bookingId)));

        if (!Objects.equals(booking.getBooker().getId(), userId) &&
                !Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new NotFoundException(String.format("User with id %x is not the owner of item or booking", userId));
        }

        return bookingMapper.toDto(booking);
    }

    @Override
    public List<BookingDto> getAllItemsBookingForUser(Long userId, String state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %x not found!", userId)));

        List<Item> items = itemRepository.findByOwnerId(userId);
        List<Long> itemsIds = items.stream().map(Item::getId).collect(Collectors.toList());
        List<Booking> bookings = new ArrayList<>();

        State stateVal = parseState(state);

        switch (stateVal) {
            case ALL:
                bookings = bookingRepository.findAllForItems(itemsIds);
                break;

            case PAST:
                bookings = bookingRepository.findPastBookingsForItems(itemsIds, LocalDateTime.now());
                break;

            case FUTURE:
                bookings = bookingRepository.findFutureBookingsForItems(itemsIds, LocalDateTime.now());
                break;

            case CURRENT:
                bookings = bookingRepository.findCurrentBookingsForItems(itemsIds, LocalDateTime.now());
                break;

            case WAITING:
                bookings = bookingRepository.findWaitingBookingsForItems(itemsIds);
                break;

            case REJECTED:
                bookings = bookingRepository.findRejectedBookingsForItems(itemsIds);
        }

        return bookings.stream().map(bookingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public BookingDto create(Long userId, BookingRequestDto bookingRequestDto) {
        Item item = itemRepository.findById(bookingRequestDto.getItemId())
                .orElseThrow(() -> new NotFoundException(
                        String.format("Item with id %x not found!", bookingRequestDto.getItemId())));

        if (!item.getAvailable()) {
            throw new AvailableException(String.format("Item with id %x is not available!", item.getId()));
        }

        if (bookingRequestDto.getStart().isAfter(bookingRequestDto.getEnd()) ||
                bookingRequestDto.getStart().isEqual(bookingRequestDto.getEnd())) {
            throw new ValidationException("The end date must be after the start date");
        }

        if (Objects.equals(item.getOwner().getId(), userId)) {
            throw new BookingItemByOwnerException("The owner of the item cannot booking it");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %x not found!", userId)));

        return bookingMapper.toDto(bookingRepository.save(bookingMapper.toBookingFromRequest(bookingRequestDto, item, user)));
    }

    @Override
    public BookingDto updateStatus(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Booking with id %x not found!", bookingId)));

        if (!Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new NotFoundException(String.format("User with id %x is not the owner of booking", userId));
        }

        if (approved && booking.getStatus() == Status.APPROVED) {
            throw new AvailableException(String.format("The booking with id %x already has the status APPROVED", bookingId));
        }

        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    private State parseState(String state) {
        try {
            return State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new StateException(String.format("Unknown state: %s", state));
        }
    }

}