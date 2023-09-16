package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<ItemDto> findByUserId(Long userId) {
        return itemRepository.findByUserId(userId).stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto findById(Long itemId) {
        return itemRepository.findById(itemId).map(itemMapper::toDto)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id %x not found!", itemId)));
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemRepository.search(text).stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %x not found!", userId)));

        itemDto.setOwner(userMapper.toDto(user));

        return itemMapper.toDto(itemRepository.create(itemMapper.toItem(itemDto)));
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %x not found!", userId)));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id %x not found!", itemId)));

        if (!Objects.equals(userId, item.getOwner().getId())) {
            throw new AccessException(
                    String.format("User with id %x is not the owner of item with id %x", userId, item.getId()));
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return itemMapper.toDto(itemRepository.update(item));
    }

}