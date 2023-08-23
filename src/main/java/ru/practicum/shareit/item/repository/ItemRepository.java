package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    List<Item> findByUserId(Long userId);

    Optional<Item> findById(Long itemId);

    List<Item> search(String text);

    Item create(Item item);

    Item update(Item item);

}
