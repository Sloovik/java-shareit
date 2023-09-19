package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.exception.AvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<ItemDto> findByUserId(Long userId) {
        return itemRepository.findByOwnerId(userId).stream()
                .map(this::getItemWithBookingsAndComments)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto findById(Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id %x not found!", itemId)));

        if (Objects.equals(item.getOwner().getId(), userId)) {
            return getItemWithBookingsAndComments(item);
        }

        return getItemWithComments(item);
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }

        return itemRepository.search(text).stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %x not found!", userId)));

        itemDto.setOwner(userMapper.toDto(user));

        return itemMapper.toDto(itemRepository.save(itemMapper.toItem(itemDto)));
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

        return itemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public CommentDto createComment(Long userId, Long itemId, CommentRequestDto commentRequestDto) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %x not found!", userId)));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id %x not found!", itemId)));

        if (bookingRepository.findBookingsForAddComments(itemId, userId, LocalDateTime.now()).isEmpty()) {
            throw new AvailableException(String.format("You don't add comment for item with id: %x", itemId));
        }

        return commentMapper.toDto(commentRepository.save(commentMapper.toComment(commentRequestDto, item, author)));
    }

    private ItemDto getItemWithComments(Item item) {
        List<Comment> comments = commentRepository.findByItemId(item.getId());

        return itemMapper.toResponseDto(item, null, null, comments);
    }

    private ItemDto getItemWithBookingsAndComments(Item item) {
        BookingItemDto lastBooking = bookingRepository.findLastBookingForItem(item.getId(), LocalDateTime.now()).stream()
                .findFirst()
                .map(bookingMapper::toBookingItemDto)
                .orElse(null);
        BookingItemDto nextBooking = bookingRepository.findNextBookingForItem(item.getId(), LocalDateTime.now()).stream()
                .findFirst()
                .map(bookingMapper::toBookingItemDto)
                .orElse(null);
        List<Comment> comments = commentRepository.findByItemId(item.getId());

        return itemMapper.toResponseDto(item, lastBooking, nextBooking, comments);
    }

}