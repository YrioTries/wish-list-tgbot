package ru.javabot.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.javabot.interaction_api.user.dto.CreateUserRequest;
import ru.javabot.interaction_api.user.dto.UserDto;
import ru.javabot.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping
    public UserDto findUserByNickname(String nickname) {
       return userService.findUserByNickname(nickname);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addNewUser(CreateUserRequest userRequest) {
        return userService.addNewUser(userRequest);
    }

    @PatchMapping
    public UserDto updateNickname(Long chatId, String newNickname) {
        return userService.updateNickname(chatId, newNickname);
    }
}
