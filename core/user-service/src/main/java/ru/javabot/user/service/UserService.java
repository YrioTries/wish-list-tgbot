package ru.javabot.user.service;

import ru.javabot.interaction_api.user.dto.CreateUserRequest;
import ru.javabot.interaction_api.user.dto.UserDto;

public interface UserService {
    UserDto findUserByUsername(String nickname);

    UserDto addNewUser(CreateUserRequest userRequest);

    UserDto updateNickname(Long chatId, String newNickname);
}
