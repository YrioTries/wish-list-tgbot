package ru.javabot.wish.service;

import ru.javabot.interaction_api.model.wish.dto.CreateWishRequest;
import ru.javabot.interaction_api.model.wish.dto.WishDto;

import java.util.List;

public interface WishService {
    List<WishDto> createWishes(Long wishlistId, List<CreateWishRequest> wishRequestList);
}
