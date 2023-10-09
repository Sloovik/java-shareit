package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingRequestDtoJsonTest {

    @Autowired
    JacksonTester<BookingRequestDto> jacksonTester;

    @Test
    void testBookingRequestDto() throws IOException {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusHours(1);

        BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
                .itemId(1L)
                .start(startDate)
                .end(endDate)
                .build();
        JsonContent<BookingRequestDto> jsonContent = jacksonTester.write(bookingRequestDto);

        assertThat(jsonContent).hasJsonPath("$.itemId");
        assertThat(jsonContent).hasJsonPath("$.start");
        assertThat(jsonContent).hasJsonPath("$.end");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.start").isEqualTo(startDate.toString());
        assertThat(jsonContent).extractingJsonPathStringValue("$.end").isEqualTo(endDate.toString());
    }

}
