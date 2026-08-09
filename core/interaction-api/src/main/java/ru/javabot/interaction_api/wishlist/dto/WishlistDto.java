package ru.javabot.interaction_api.wishlist.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.javabot.interaction_api.wish.dto.WishDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishlistDto {
    String name;
    AccessRights accessRights;
    List<WishDto> gifts;
}