package ru.javabot.interaction_api.wish.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishDto {

    Long id;
    Long wishlistId;
    String name;
    Long referenceId;
    WishStatus status;
    Long expectedPrice;
    String description;
}
