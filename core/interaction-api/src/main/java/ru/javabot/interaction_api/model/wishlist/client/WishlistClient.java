package ru.javabot.interaction_api.model.wishlist.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.javabot.interaction_api.model.wishlist.dto.CreateWishlistRequest;
import ru.javabot.interaction_api.model.wishlist.dto.WishlistDto;

import java.util.List;

@FeignClient(name = "wishlist-service", path = "/presents/wishlist")
public interface WishlistClient {
    @GetMapping
    List<WishlistDto> showWishlistsOfUser(Long ownerId);

    @PostMapping
    public WishlistDto addNewWishlist(Long ownerId, @RequestBody CreateWishlistRequest wishlistRequest);
}
