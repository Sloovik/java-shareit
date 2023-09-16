package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final ItemService itemService;

    @GetMapping()
    public List<ItemDto> findByUserId(@RequestHeader(name = USER_ID_HEADER) Long userId) {
        log.info("GET request to find items by user id: {}", userId);
        return itemService.findByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable Long itemId) {
        log.info("GET request to find item by item id: {}", itemId);
        return itemService.findById(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") String text) {
        log.info("GET request to find item by potential user: {}", text);
        return itemService.search(text);
    }

    @PostMapping()
    public ItemDto create(@RequestHeader(name = USER_ID_HEADER) Long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("POST request to add item by user id: {} for item: {}", userId, itemDto);
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(name = USER_ID_HEADER) Long userId,
                          @PathVariable Long itemId,
                          @RequestBody ItemDto itemDto) {
        log.info("PATCH request to update item by user id: {} for item id: {} and item: {}", userId, itemId, itemDto);
        return itemService.update(userId, itemId, itemDto);
    }

}
