package ru.practicum.shareit.item.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> findByUserId(Long userId, Pageable pageable);

    ItemDto findById(Long userId, Long itemId);

    List<ItemDto> search(String text, Pageable pageable);

    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    CommentDto createComment(Long userId, Long itemId, CommentRequestDto commentRequestDto);

}

