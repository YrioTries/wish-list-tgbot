package ru.javabot.wishlist.dao;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.javabot.interaction_api.wishlist.dto.CreateWishlistRequest;
import ru.javabot.interaction_api.wishlist.dto.WishlistDto;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface WishlistMapper {
    WishlistDao toDao(CreateWishlistRequest wishlistRequest);

    WishlistDto toDto(WishlistDao wishlistDao);
}

