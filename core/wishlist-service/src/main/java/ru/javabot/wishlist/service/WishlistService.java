package ru.javabot.wishlist.service;

import ru.javabot.interaction_api.wishlist.dto.CreateWishlistRequest;
import ru.javabot.interaction_api.wishlist.dto.WishlistDto;

import java.util.List;

public interface WishlistService {
    List<WishlistDto> showWishlists(Long ownerId);

    WishlistDto addNewWishlist(Long ownerId, CreateWishlistRequest wishlistRequest);
}
