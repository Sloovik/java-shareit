package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

import java.util.List;

public interface ItemRequestService {

    List<ItemRequestWithItemsDto> findAllByUserId(Long userId);

    List<ItemRequestWithItemsDto> findAll(Long userId, Pageable pageable);

    ItemRequestWithItemsDto findById(Long userId, Long requestId);

    ItemRequestResponseDto create(Long userId, CreateItemRequestDto itemRequestDto);

}
