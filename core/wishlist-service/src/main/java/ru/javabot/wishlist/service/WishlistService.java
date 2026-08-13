package ru.javabot.wishlist.service;

import ru.javabot.interaction_api.wishlist.dto.CreateWishlistRequest;
import ru.javabot.interaction_api.wishlist.dto.WishlistDto;

public interface WishlistService {
    WishlistDto addNewWish(Long ownerId, CreateWishlistRequest wishlistRequest);
}
