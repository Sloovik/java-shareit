package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemInRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
public class ItemRequestWithItemsDto {

    private Long id;

    private String description;

    private LocalDateTime created;

    private List<ItemInRequestDto> items;

}
