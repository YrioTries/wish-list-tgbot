package ru.javabot.wish.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.javabot.interaction_api.wish.dto.CreateWishRequest;
import ru.javabot.interaction_api.wish.dto.WishDto;
import ru.javabot.wish.service.WishServiceImpl;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/presents/wish")
public class WishController {
    private final WishServiceImpl wishService;

    @PostMapping("/wishlist/{wishlistId}")
    public List<WishDto> createNewWish(@PathVariable Long wishlistId,
                                       @RequestBody List<CreateWishRequest> wishRequestList) {
        return wishService.createWishes(wishlistId, wishRequestList);
    }
}
