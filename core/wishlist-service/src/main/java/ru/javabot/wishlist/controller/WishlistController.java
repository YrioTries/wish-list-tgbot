package ru.javabot.wishlist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.javabot.interaction_api.wishlist.dto.CreateWishlistRequest;
import ru.javabot.interaction_api.wishlist.dto.WishlistDto;
import ru.javabot.wishlist.service.WishlistService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/presents/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;

    @GetMapping
    public List<WishlistDto> showWishlistsOfUser(Long ownerId) {
        return wishlistService.showWishlists(ownerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WishlistDto addNewWishlist(Long ownerId, @RequestBody CreateWishlistRequest wishlistRequest) {
        return wishlistService.addNewWishlist(ownerId, wishlistRequest);
    }
}
