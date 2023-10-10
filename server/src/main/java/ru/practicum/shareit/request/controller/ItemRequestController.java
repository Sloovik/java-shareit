package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.utils.HttpHeaders;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private static final String DEFAULT_FROM_VALUE = "0";
    private static final String DEFAULT_SIZE_VALUE = "20";

    private final ItemRequestService itemRequestService;

    @GetMapping
    public List<ItemRequestWithItemsDto> findAllByUserId(@RequestHeader(HttpHeaders.USER_ID_HEADER) Long userId) {
        return itemRequestService.findAllByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestWithItemsDto> findAll(@RequestHeader(HttpHeaders.USER_ID_HEADER) Long userId,
                                                 @RequestParam(defaultValue = DEFAULT_FROM_VALUE) int from,
                                                 @RequestParam(defaultValue = DEFAULT_SIZE_VALUE) int size) {
        PageRequest pageable = PageRequest.of(from / size, size, Sort.by("created").descending());

        return itemRequestService.findAll(userId, pageable);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithItemsDto findById(@RequestHeader(HttpHeaders.USER_ID_HEADER) Long userId,
                                            @PathVariable Long requestId) {
        return itemRequestService.findById(userId, requestId);
    }

    @PostMapping
    public ItemRequestResponseDto createRequest(@RequestHeader(HttpHeaders.USER_ID_HEADER) Long userId,
                                                @RequestBody CreateItemRequestDto createItemRequestDto) {
        return itemRequestService.create(userId, createItemRequestDto);
    }

}
