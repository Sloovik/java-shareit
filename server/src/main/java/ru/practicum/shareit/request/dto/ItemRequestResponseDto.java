package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ItemRequestResponseDto {

    private Long id;

    private String description;

    private LocalDateTime created;

}
