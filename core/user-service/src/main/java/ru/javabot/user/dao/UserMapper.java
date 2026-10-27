package ru.javabot.user.dao;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.javabot.interaction_api.model.user.dto.UserRequest;
import ru.javabot.interaction_api.model.user.dto.UserDto;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    UserDto toDto(UserDao userDao);

    UserDao toDao(UserRequest userRequest);
}
