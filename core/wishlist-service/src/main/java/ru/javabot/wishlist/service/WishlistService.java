package ru.javabot.wishlist.service;

import ru.javabot.interaction_api.wishlist.dto.CreateWishlistRequest;
import ru.javabot.interaction_api.wishlist.dto.WishlistDto;

public interface WishlistService {
    WishlistDto addNewWishlist(Long ownerId, CreateWishlistRequest wishlistRequest);
}
