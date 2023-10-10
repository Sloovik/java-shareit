package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ItemRequestDto {

    private Long id;

    private String description;

    private UserDto requestor;

    private LocalDateTime created;

}
