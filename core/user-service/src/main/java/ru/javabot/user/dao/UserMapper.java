package ru.javabot.user.dao;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.javabot.interaction_api.user.dto.UserDto;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    UserDto toDto(UserDao UserDao);

    UserDao toDao(UserDto UserDto);
}
