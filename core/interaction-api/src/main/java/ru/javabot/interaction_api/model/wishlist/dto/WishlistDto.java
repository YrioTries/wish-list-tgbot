package ru.javabot.interaction_api.model.wishlist.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishlistDto {
    Long id;
    Long ownerId;
    String name;
    AccessRights accessRights;
}