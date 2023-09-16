package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@Builder
public class Item {

    private Long id;

    @NotBlank
    private String name;

    private String description;

    private Boolean available;

    private User owner;

    private ItemRequest request;

}