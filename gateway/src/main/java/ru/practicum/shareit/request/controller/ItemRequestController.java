package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.utils.HttpHeaders;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private static final String DEFAULT_FROM_VALUE = "0";
    private static final String DEFAULT_SIZE_VALUE = "20";

    private final ItemRequestClient itemRequestClient;

    @GetMapping
    public ResponseEntity<Object> findAllByUserId(@RequestHeader(HttpHeaders.USER_ID_HEADER) Long userId) {
        return itemRequestClient.findAllByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll(@RequestHeader(HttpHeaders.USER_ID_HEADER) Long userId,
                                          @PositiveOrZero @RequestParam(defaultValue = DEFAULT_FROM_VALUE) int from,
                                          @Positive @RequestParam(defaultValue = DEFAULT_SIZE_VALUE) int size) {
        return itemRequestClient.findAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(@RequestHeader(HttpHeaders.USER_ID_HEADER) Long userId,
                                           @PathVariable Long requestId) {
        return itemRequestClient.findById(userId, requestId);
    }

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader(HttpHeaders.USER_ID_HEADER) Long userId,
                                                @Valid @RequestBody CreateItemRequestDto createItemRequestDto) {
        return itemRequestClient.createRequest(userId, createItemRequestDto);
    }

}
