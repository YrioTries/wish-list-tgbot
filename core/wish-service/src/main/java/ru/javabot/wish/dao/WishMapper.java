package ru.javabot.wish.dao;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.javabot.interaction_api.wish.dto.WishDto;


@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface WishMapper {

    WishDto toDto(WishDao UserDao);

    WishDao toDao(WishDto WishDto);
}
