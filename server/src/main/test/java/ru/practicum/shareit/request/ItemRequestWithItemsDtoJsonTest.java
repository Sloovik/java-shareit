package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestWithItemsDtoJsonTest {

    @Autowired
    JacksonTester<ItemRequestWithItemsDto> jacksonTester;

    @Test
    void testItemRequestWithItemsDto() throws IOException {
        LocalDateTime created = LocalDateTime.now();

        ItemRequestWithItemsDto itemRequestWithItemsDto = ItemRequestWithItemsDto.builder()
                .id(1L)
                .description("test")
                .created(created)
                .build();

        JsonContent<ItemRequestWithItemsDto> jsonContent = jacksonTester.write(itemRequestWithItemsDto);

        assertThat(jsonContent).hasJsonPath("$.id");
        assertThat(jsonContent).hasJsonPath("$.description");
        assertThat(jsonContent).hasJsonPath("$.created");

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("test");
        assertThat(jsonContent).extractingJsonPathStringValue("$.created").isEqualTo(created.toString());
    }

}
