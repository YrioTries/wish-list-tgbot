package ru.javabot.interaction_api.model.wish.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.javabot.interaction_api.model.wish.dto.CreateWishRequest;
import ru.javabot.interaction_api.model.wish.dto.WishDto;

import java.util.List;

@FeignClient(name = "wish-service", path = "/presents/wish")
public interface WishClient {

    @PostMapping("/wishlist/{wishlistId}")
    List<WishDto> createNewWish(@PathVariable Long wishlistId,
                                       @RequestBody List<CreateWishRequest> wishRequestList);
}
