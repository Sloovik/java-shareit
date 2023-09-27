package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoJsonTest {
    @Autowired
    JacksonTester<ItemDto> jacksonTester;

    @Test
    void testItemResponseDto() throws IOException {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("test")
                .description("test")
                .requestId(3L)
                .available(true)
                .build();
        JsonContent<ItemDto> jsonContent = jacksonTester.write(itemDto);

        assertThat(jsonContent).hasJsonPath("$.id");
        assertThat(jsonContent).hasJsonPath("$.name");
        assertThat(jsonContent).hasJsonPath("$.description");
        assertThat(jsonContent).hasJsonPath("$.available");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("test");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("test");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isTrue();
    }
}
