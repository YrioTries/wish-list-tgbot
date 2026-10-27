package ru.javabot.user.service;

import ru.javabot.interaction_api.model.user.dto.UserRequest;
import ru.javabot.interaction_api.model.user.dto.UserDto;

public interface UserService {
    UserDto findUserByNickname(String nickname);

    UserDto addNewUser(UserRequest userRequest);

    UserDto updateNickname(UserRequest request);
}
