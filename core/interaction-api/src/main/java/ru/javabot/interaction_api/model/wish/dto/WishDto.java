package ru.javabot.interaction_api.model.wish.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
