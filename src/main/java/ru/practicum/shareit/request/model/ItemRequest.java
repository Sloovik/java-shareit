package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@Builder
public class ItemRequest {

    private Long id;

    private String description;

    private User requestor;

    private LocalDate created;

}
