package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.utils.HttpHeaders;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping()
    public List<ItemDto> findByUserId(@RequestHeader(name = HttpHeaders.USER_ID_HEADER) Long userId,
                                      @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                      @Positive @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id"));

        return itemService.findByUserId(userId, pageable);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@RequestHeader(name = HttpHeaders.USER_ID_HEADER) Long userId, @PathVariable Long itemId) {
        return itemService.findById(userId, itemId);
    }


    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") String text,
                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                @Positive @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id"));

        return itemService.search(text, pageable);
    }

    @PostMapping()
    public ItemDto create(@RequestHeader(name = HttpHeaders.USER_ID_HEADER) Long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("POST request to add item by user id: {} for item: {}", userId, itemDto);
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(name = HttpHeaders.USER_ID_HEADER) Long userId,
                          @PathVariable Long itemId,
                          @RequestBody ItemDto itemDto) {
        log.info("PATCH request to update item by user id: {} for item id: {} and item: {}", userId, itemId, itemDto);
        return itemService.update(userId, itemId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(name = HttpHeaders.USER_ID_HEADER) Long userId,
                                    @PathVariable Long itemId,
                                    @Valid @RequestBody CommentRequestDto commentDto) {
        log.info("POST request to create comment by userId: {} for itemId: {} and commentDto: {}", userId, itemId, commentDto);
        return itemService.createComment(userId, itemId, commentDto);
    }
}
