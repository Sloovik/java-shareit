package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exeption.AvailableException;
import ru.practicum.shareit.exeption.BookingItemByOwnerException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingServiceImplTest {

    private final EntityManager em;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;

    @InjectMocks
    private final BookingServiceImpl bookingServiceImpl;

    private BookingRequestDto bookingRequestDto;

    private final LocalDateTime start = LocalDateTime.now();
    private final LocalDateTime end = start.plusDays(2);
    private final Pageable pageRequest = PageRequest.of(0, 10, Sort.by("startDate").descending());

    @BeforeEach
    void init() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("test")
                .email("test@email.ru")
                .build();

        userService.create(userDto);

        userService.create(UserDto.builder()
                .id(2L)
                .name("test")
                .email("test1@email.ru")
                .build());

        itemService.create(2L, ItemDto.builder()
                .id(1L)
                .name("test")
                .description("test")
                .available(true)
                .owner(userDto)
                .requestId(null)
                .build());

        bookingRequestDto = BookingRequestDto.builder()
                .start(start)
                .end(end)
                .itemId(1L)
                .build();
    }

    @Test
    void testGetAllBookingsForUserStatusAll() {
        bookingService.create(1L, bookingRequestDto);

        List<BookingDto> bookingList = bookingService.getAllBookingsForUser(1L, State.ALL, pageRequest);

        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getId(), 1L);
    }

    @Test
    void testGetAllBookingsForUserStatusPast() {
        bookingRequestDto.setEnd(end.plusDays(1));

        bookingService.create(1L, bookingRequestDto);

        List<BookingDto> bookingList = bookingService.getAllBookingsForUser(1L, State.PAST, pageRequest);

        assertEquals(bookingList.size(), 0);
    }

    @Test
    void testGetAllBookingsForUserStatusFuture() {
        bookingRequestDto.setStart(start.plusDays(1));

        bookingService.create(1L, bookingRequestDto);

        List<BookingDto> bookingList = bookingService.getAllBookingsForUser(1L, State.FUTURE, pageRequest);

        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getId(), 1L);
    }

    @Test
    void testGetAllBookingsForUserStatusCurrent() {
        bookingService.create(1L, bookingRequestDto);

        List<BookingDto> bookingList = bookingService.getAllBookingsForUser(1L, State.CURRENT, pageRequest);

        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getId(), 1L);
    }

    @Test
    void testGetAllBookingsForUserStatusWaiting() {
        bookingService.create(1L, bookingRequestDto);

        List<BookingDto> bookingList = bookingService.getAllBookingsForUser(1L, State.WAITING, pageRequest);

        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getId(), 1L);
    }

    @Test
    void testGetAllBookingsForUserStatusRejected() {
        bookingService.create(1L, bookingRequestDto);
        bookingService.updateStatus(2L, 1L, false);

        List<BookingDto> bookingList = bookingService.getAllBookingsForUser(1L, State.REJECTED, pageRequest);

        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getId(), 1L);
    }

    @Test
    void testGetAllBookingsForUserNotFoundUserError() {
        bookingService.create(1L, bookingRequestDto);

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                bookingService.getAllBookingsForUser(3L, State.ALL, pageRequest));

        assertEquals(exception.getMessage(), String.format("User with id %x not found!", 3L));
    }

    @Test
    void testGetBooking() {
        bookingService.create(1L, bookingRequestDto);

        BookingDto booking = bookingService.getBooking(2L, 1L);

        assertEquals(booking.getId(), 1L);
    }

    @Test
    void testGetBookingNotFoundBookingError() {
        bookingService.create(1L, bookingRequestDto);

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                bookingService.getBooking(1L, 2L));

        assertEquals(exception.getMessage(), String.format("Booking with id %x not found!", 2L));
    }

    @Test
    void testGetAllItemsBookingForUserStatusAll() {
        bookingService.create(1L, bookingRequestDto);

        List<BookingDto> bookingList = bookingService.getAllItemsBookingForUser(2L, State.ALL, pageRequest);

        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getId(), 1L);
    }

    @Test
    void testGetAllItemsBookingUserStatusPast() {
        bookingService.create(1L, bookingRequestDto);

        List<BookingDto> bookingList = bookingService.getAllItemsBookingForUser(1L, State.PAST, pageRequest);

        assertEquals(bookingList.size(), 0);
    }

    @Test
    void testGetAllItemsBookingUserStatusFuture() {
        bookingRequestDto.setStart(start.plusDays(1));

        bookingService.create(1L, bookingRequestDto);

        List<BookingDto> bookingList = bookingService.getAllItemsBookingForUser(2L, State.FUTURE, pageRequest);

        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getId(), 1L);
    }

    @Test
    void testGetAllItemsBookingUserStatusCurrent() {
        bookingService.create(1L, bookingRequestDto);

        List<BookingDto> bookingList = bookingService.getAllItemsBookingForUser(2L, State.CURRENT, pageRequest);

        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getId(), 1L);
    }

    @Test
    void testGetAllItemsBookingUserStatusWaiting() {
        bookingService.create(1L, bookingRequestDto);

        List<BookingDto> bookingList = bookingService.getAllItemsBookingForUser(2L, State.WAITING, pageRequest);

        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getId(), 1L);
    }

    @Test
    void testGetAllItemsBookingUserStatusRejected() {
        bookingService.create(1L, bookingRequestDto);
        bookingService.updateStatus(2L, 1L, false);

        List<BookingDto> bookingList = bookingService.getAllItemsBookingForUser(2L, State.REJECTED, pageRequest);

        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getId(), 1L);
    }

    @Test
    void testGetAllItemsBookingUserNotFoundUserError() {
        bookingService.create(1L, bookingRequestDto);

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                bookingService.getAllItemsBookingForUser(3L, State.ALL, pageRequest));

        assertEquals(exception.getMessage(), String.format("User with id %x not found!", 3L));
    }

    @Test
    void testCreate() {
        bookingService.create(1L, bookingRequestDto);

        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.id = :id", Booking.class);
        Booking booking = query.setParameter("id", 1L).getSingleResult();

        assertEquals(booking.getStartDate(), start);
        assertEquals(booking.getEndDate(), end);
        assertEquals(booking.getItem().getId(), 1L);
        assertEquals(booking.getStatus(), Status.WAITING);
    }

    @Test
    void testCreateUserNotFoundError() {
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                bookingService.create(3L, bookingRequestDto));

        assertEquals(exception.getMessage(), String.format("User with id %x not found!", 3L));
    }

    @Test
    void testCreateOwnerNotFoundError() {
        BookingItemByOwnerException exception = assertThrows(BookingItemByOwnerException.class, () ->
                bookingService.create(2L, bookingRequestDto));

        assertEquals(exception.getMessage(), "The owner of the item cannot booking it");
    }

    @Test
    void testCreateItemNotFoundError() {
        bookingRequestDto.setItemId(10L);
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                bookingService.create(2L, bookingRequestDto));

        assertEquals(exception.getMessage(), String.format("Item with id %x not found!", 10L));
    }

    @Test
    void testCreateDateValidationError() {
        bookingRequestDto.setStart(end);
        bookingRequestDto.setEnd(start);

        ValidationException exception = assertThrows(ValidationException.class, () ->
                bookingService.create(1L, bookingRequestDto));

        assertEquals(exception.getMessage(), "The end date must be after the start date");
    }

    @Test
    void testCreateItemAvailableError() {
        UserDto userDto = UserDto.builder()
                .id(3L)
                .name("test")
                .email("test3@email.ru")
                .build();

        itemService.create(1L, ItemDto.builder()
                .id(2L)
                .name("test")
                .description("test")
                .available(false)
                .owner(userDto)
                .build());

        bookingRequestDto.setItemId(2L);

        AvailableException exception = assertThrows(AvailableException.class, () ->
                bookingService.create(1L, bookingRequestDto));

        assertEquals(exception.getMessage(), String.format("Item with id %x is not available!", 2L));
    }

    @Test
    void testUpdateStatus() {
        User user = User.builder()
                .id(1L)
                .name("test")
                .email("test@email.ru")
                .build();
        User user2 = User.builder()
                .id(2L)
                .name("test")
                .email("test@email.ru")
                .build();
        Item item = Item.builder()
                .id(1L)
                .name("test")
                .description("test")
                .available(true)
                .owner(user)
                .requestId(1L)
                .build();
        Booking booking = Booking.builder()
                .id(1L)
                .startDate(start)
                .endDate(end)
                .item(item)
                .booker(user2)
                .status(Status.WAITING)
                .build();

        bookingService.create(1L, bookingRequestDto);

        BookingDto bookingDto = bookingServiceImpl.updateStatus(2L, booking.getId(), true);

        assertEquals(Status.APPROVED, bookingDto.getStatus());
    }

    @Test
    void testUpdateStatusBookingNotFoundError() {
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                bookingService.updateStatus(1L, 3L, true));

        assertEquals(exception.getMessage(), String.format("Booking with id %x not found!", 3L));
    }

    @Test
    void testUpdateStatusOwnerNotFoundError() {
        bookingService.create(1L, bookingRequestDto);

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                bookingServiceImpl.updateStatus(1L, 1L, true));

        assertEquals(exception.getMessage(), String.format("User with id %x is not the owner of booking", 1L));
    }

}
