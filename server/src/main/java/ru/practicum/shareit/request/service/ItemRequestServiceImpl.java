package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemInRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository requestRepository;

    public List<ItemRequestWithItemsDto> findAllByUserId(Long userId) {
        getUserThrowable(userId);

        List<ItemRequest> requests = requestRepository.findItemRequestByRequestorId(userId,
                Sort.by("created").descending());

        return getItemsForItemRequestList(requests);
    }

    public List<ItemRequestWithItemsDto> findAll(Long userId, Pageable pageable) {
        getUserThrowable(userId);

        List<ItemRequest> itemRequests = requestRepository.findAllByRequestorIdIsNot(userId, pageable);

        return getItemsForItemRequestList(itemRequests);
    }

    public ItemRequestWithItemsDto findById(Long userId, Long requestId) {
        getUserThrowable(userId);

        ItemRequest itemRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id %x not found!", requestId)));

        List<Item> items = itemRepository.findByRequestId(requestId);

        return ItemRequestMapper.toItemRequestWithItemsDto(itemRequest,
                items.stream().map(ItemMapper::toItemInRequestDto).collect(Collectors.toList()));
    }

    @Transactional
    public ItemRequestResponseDto create(Long userId, CreateItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = ItemRequestMapper.createItemRequestToItemRequest(itemRequestDto);
        User user = getUserThrowable(userId);

        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());

        return ItemRequestMapper.toItemRequestResponse(requestRepository.save(itemRequest));
    }

    private List<ItemRequestWithItemsDto> getItemsForItemRequestList(List<ItemRequest> itemRequests) {
        List<Item> items = itemRepository.findByRequestIdIn(itemRequests.stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList()));
        Map<Long, List<Item>> itemsByItemRequestId = items.stream().collect(Collectors.groupingBy(Item::getRequestId));
        List<ItemRequestWithItemsDto> itemRequestsWithItems = new LinkedList<>();

        for (ItemRequest itemRequest : itemRequests) {
            List<Item> itemsForItemRequest = itemsByItemRequestId.getOrDefault(itemRequest.getId(), Collections.emptyList());
            List<ItemInRequestDto> itemsInRequest = itemsForItemRequest.stream()
                    .map(ItemMapper::toItemInRequestDto).collect(Collectors.toList());

            itemRequestsWithItems.add(ItemRequestMapper.toItemRequestWithItemsDto(itemRequest, itemsInRequest));
        }

        return itemRequestsWithItems;
    }

    private User getUserThrowable(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %x not found!", userId)));
    }

}
