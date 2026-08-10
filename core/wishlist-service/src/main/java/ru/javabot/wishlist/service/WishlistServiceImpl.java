package ru.javabot.wishlist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javabot.interaction_api.wishlist.dto.WishlistDto;
import ru.javabot.wishlist.repository.WishlistRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishlistServiceImpl {
    private final WishlistRepository wishlistRepository;

    @Transactional
    public WishlistDto addNewWish() {
        return new WishlistDto();
    }
}
