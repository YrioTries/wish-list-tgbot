package ru.javabot.user.service;

import ru.javabot.interaction_api.model.user.dto.CreateUserRequest;
import ru.javabot.interaction_api.model.user.dto.UserDto;

public interface UserService {
    UserDto findUserByNickname(String nickname);

    UserDto addNewUser(CreateUserRequest userRequest);

    UserDto updateNickname(Long chatId, String newNickname);
}
