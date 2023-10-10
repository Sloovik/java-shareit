package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.utils.HttpHeaders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private BookingDto bookingDto;
    private BookingRequestDto bookingRequestDto;

    private final Pageable pageRequest = PageRequest.of(0, 10, Sort.by("startDate").descending());

    @BeforeEach
    void init() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusDays(1);

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("test")
                .email("test@email.ru")
                .build();
        ItemDto item = ItemDto.builder()
                .id(1L)
                .name("test")
                .available(true)
                .build();

        bookingDto = BookingDto.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(item)
                .booker(userDto)
                .status(Status.APPROVED)
                .build();

        bookingRequestDto = BookingRequestDto.builder()
                .itemId(item.getId())
                .start(start)
                .end(end)
                .build();
    }

    @Test
    void testGetAllBookingsForUser() throws Exception {
        Mockito
                .when(bookingService.getAllBookingsForUser(Mockito.anyLong(), Mockito.any(State.class), Mockito.any(Pageable.class)))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings")
                        .header(HttpHeaders.USER_ID_HEADER, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().toString()))
                .andExpect(jsonPath("$[0].item.id").value(bookingDto.getItem().getId()))
                .andDo(print());

        Mockito.verify(bookingService).getAllBookingsForUser(1L, State.ALL, pageRequest);
    }

    @Test
    void testGetBooking() throws Exception {
        Mockito
                .when(bookingService.getBooking(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .header(HttpHeaders.USER_ID_HEADER, "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDto)));

        Mockito.verify(bookingService).getBooking(1L, 1L);
    }

    @Test
    void testGetAllItemsBookingForUser() throws Exception {
        Mockito
                .when(bookingService.getAllItemsBookingForUser(Mockito.anyLong(), Mockito.any(State.class),
                        Mockito.any(Pageable.class))).thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings/owner")
                        .header(HttpHeaders.USER_ID_HEADER, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().toString()))
                .andExpect(jsonPath("$[0].item.id").value(bookingDto.getItem().getId()))
                .andDo(print());

        Mockito.verify(bookingService).getAllItemsBookingForUser(1L, State.ALL, pageRequest);
    }

    @Test
    void testCreate() throws Exception {
        Mockito
                .when(bookingService.create(Mockito.anyLong(), Mockito.any())).thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.USER_ID_HEADER, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDto)));

        Mockito.verify(bookingService).create(1L, bookingRequestDto);
    }

    @Test
    void testUpdateStatus() throws Exception {
        Mockito
                .when(bookingService.updateStatus(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(bookingDto);

        mvc.perform(patch("/bookings/{bookingId}?approved=true", 1L)
                        .content(objectMapper.writeValueAsString(bookingRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.USER_ID_HEADER, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDto)));

        Mockito.verify(bookingService).updateStatus(1L, 1L, true);
    }

}
