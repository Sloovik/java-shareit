package ru.practicum.shareit.request.dto;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemRequestDto {

    private String description;

}
