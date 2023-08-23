package ru.practicum.shareit.request.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.mapper.UserMapper;

@Component
@AllArgsConstructor
public class ItemRequestMapper {

    private final UserMapper userMapper;

    public ItemRequestDto toDto(ItemRequest item) {
        return ItemRequestDto.builder()
                .id(item.getId())
                .description(item.getDescription())
                .requestor(userMapper.toDto(item.getRequestor()))
                .created(item.getCreated())
                .build();
    }

    public ItemRequest toItemRequest(ItemRequestDto item) {
        return ItemRequest.builder()
                .id(item.getId())
                .description(item.getDescription())
                .requestor(userMapper.toUser(item.getRequestor()))
                .created(item.getCreated())
                .build();
    }

}