package ru.javabot.wishlist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javabot.interaction_api.wishlist.dto.CreateWishlistRequest;
import ru.javabot.interaction_api.wishlist.dto.WishlistDto;
import ru.javabot.wishlist.service.WishlistService;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/presents/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;

    @PostMapping
    public WishlistDto addNewWish(Long ownerId, @RequestBody CreateWishlistRequest wishlistRequest) {
        return wishlistService.addNewWish(ownerId, wishlistRequest);
    }
}
