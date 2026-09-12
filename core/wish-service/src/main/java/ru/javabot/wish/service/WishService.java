package ru.javabot.wish.service;

import ru.javabot.interaction_api.wish.dto.CreateWishRequest;
import ru.javabot.interaction_api.wish.dto.WishDto;

import java.util.List;

public interface WishService {
    List<WishDto> createWishes(Long wishlistId, List<CreateWishRequest> wishRequestList);
}
