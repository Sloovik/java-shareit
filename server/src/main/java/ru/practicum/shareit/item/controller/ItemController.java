package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.utils.HttpHeaders;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping()
    public List<ItemDto> findByUserId(@RequestHeader(name = HttpHeaders.USER_ID_HEADER) Long userId,
                                      @RequestParam(defaultValue = "0") Integer from,
                                      @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id"));

        return itemService.findByUserId(userId, pageable);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@RequestHeader(name = HttpHeaders.USER_ID_HEADER) Long userId, @PathVariable Long itemId) {
        return itemService.findById(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") String text,
                                @RequestParam(defaultValue = "0") Integer from,
                                @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id"));

        return itemService.search(text, pageable);
    }

    @PostMapping()
    public ItemDto create(@RequestHeader(name = HttpHeaders.USER_ID_HEADER) Long userId,
                          @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(name = HttpHeaders.USER_ID_HEADER) Long userId,
                          @PathVariable Long itemId,
                          @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(name = HttpHeaders.USER_ID_HEADER) Long userId,
                                    @PathVariable Long itemId,
                                    @RequestBody CommentRequestDto commentDto) {
        return itemService.createComment(userId, itemId, commentDto);
    }

}
