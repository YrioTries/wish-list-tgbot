package ru.javabot.wish.dao;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.javabot.interaction_api.wish.dto.CreateWishRequest;
import ru.javabot.interaction_api.wish.dto.WishDto;


@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface WishMapper {

    @Mapping(target = "referenceId", source = "reference.id")
    WishDto toDto(WishDao UserDao);

    @Mapping(target = "reference", ignore = true)
    WishDao toDao(CreateWishRequest createWishRequest);
}
