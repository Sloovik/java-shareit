package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class BookingMapperTest {

    private User user;
    private Item item;
    private Booking booking;
    private BookingRequestDto bookingRequestDto;

    @BeforeEach
    void init() {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusHours(1);

        user = User.builder()
                .id(1L)
                .name("test")
                .email("test@email.ru")
                .build();


        item = Item.builder()
                .id(1L)
                .name("test")
                .description("test")
                .available(true)
                .owner(user)
                .build();

        booking = Booking.builder()
                .id(1L)
                .startDate(startDate)
                .endDate(endDate)
                .item(item)
                .booker(user)
                .status(Status.APPROVED)
                .build();

        bookingRequestDto = BookingRequestDto.builder()
                .itemId(item.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(1))
                .build();
    }

    @Test
    void testToDto() {
        BookingDto dto = BookingMapper.toDto(booking);

        assertNotNull(dto);
        assertEquals(booking.getId(), dto.getId());
        assertEquals(booking.getItem().getId(), dto.getItem().getId());
        assertEquals(booking.getStartDate(), dto.getStart());
        assertEquals(booking.getEndDate(), dto.getEnd());
        assertEquals(booking.getItem().getId(), dto.getItem().getId());
        assertEquals(booking.getBooker().getId(), dto.getBooker().getId());
        assertEquals(booking.getStatus(), dto.getStatus());

        booking.setItem(null);

        BookingDto dto1 = BookingMapper.toDto(booking);

        assertEquals(booking.getItem(), dto1.getItem());
    }

    @Test
    void testToBookingItemDto() {
        BookingItemDto dto = BookingMapper.toBookingItemDto(booking);

        assertNotNull(dto);
        assertEquals(booking.getId(), dto.getId());
        assertEquals(booking.getStartDate(), dto.getStart());
        assertEquals(booking.getEndDate(), dto.getEnd());
        assertEquals(booking.getBooker().getId(), dto.getBookerId());
    }

    @Test
    void testToBookingFromRequest() {
        Booking model = BookingMapper.toBookingFromRequest(bookingRequestDto, item, user);

        assertNotNull(model);
        assertEquals(bookingRequestDto.getId(), model.getId());
        assertEquals(bookingRequestDto.getItemId(), model.getItem().getId());
        assertEquals(bookingRequestDto.getStart(), model.getStartDate());
        assertEquals(bookingRequestDto.getEnd(), model.getEndDate());
    }

}
