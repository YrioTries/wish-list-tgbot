package ru.javabot.interaction_api.wish.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishDto {
    Long id;
    @NotBlank
    @Size(min = 3, max = 50)
    String name;
    String description;
}
