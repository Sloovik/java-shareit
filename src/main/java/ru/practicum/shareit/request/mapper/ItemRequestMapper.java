package ru.practicum.shareit.request.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemInRequestDto;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;

@Component
@AllArgsConstructor
public class ItemRequestMapper {

    public static ItemRequestDto toDto(ItemRequest item) {
        return ItemRequestDto.builder()
                .id(item.getId())
                .description(item.getDescription())
                .requestor(UserMapper.toDto(item.getRequestor()))
                .created(item.getCreated())
                .build();
    }

    public static ItemRequest createItemRequestToItemRequest(CreateItemRequestDto createItemRequestDto) {
        return ItemRequest.builder()
                .description(createItemRequestDto.getDescription())
                .build();
    }

    public static ItemRequestResponseDto toItemRequestResponse(ItemRequest itemRequest) {
        return ItemRequestResponseDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
    }

    public static ItemRequestWithItemsDto toItemRequestWithItemsDto(ItemRequest itemRequest, List<ItemInRequestDto> items) {
        return ItemRequestWithItemsDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(items)
                .build();
    }

}