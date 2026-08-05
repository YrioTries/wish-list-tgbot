package ru.javabot.user.service;

import ru.javabot.interaction_api.user.dto.UserDto;

public interface UserService {
    UserDto findUserByUsername(String nickname);
}
