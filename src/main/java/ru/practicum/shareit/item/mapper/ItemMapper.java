package ru.practicum.shareit.item.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

@Component
@AllArgsConstructor
public class ItemMapper {

    private final UserMapper userMapper;
    private final ItemRequestMapper itemRequestMapper;

    public ItemDto toDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(userMapper.toDto(item.getOwner()))
                .request(item.getRequest() != null ? itemRequestMapper.toDto(item.getRequest()) : null)
                .build();
    }

    public Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(userMapper.toUser(itemDto.getOwner()))
                .request(itemDto.getRequest() != null ? itemRequestMapper.toItemRequest(itemDto.getRequest()) : null)
                .build();
    }

}