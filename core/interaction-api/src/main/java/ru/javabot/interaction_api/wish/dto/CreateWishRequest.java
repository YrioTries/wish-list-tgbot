package ru.javabot.interaction_api.wish.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateWishRequest {

    @NotBlank
    @Size(min = 3, max = 150)
    String name;

    String reference;

    @PositiveOrZero
    Long expectedPrice;

    @Size(max = 1000)
    String description;
}
