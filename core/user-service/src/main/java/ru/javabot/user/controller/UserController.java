package ru.javabot.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.javabot.interaction_api.model.user.dto.UserRequest;
import ru.javabot.interaction_api.model.user.dto.UserDto;
import ru.javabot.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public UserDto findUserByNickname(@RequestParam("nickname") String nickname) {
       return userService.findUserByNickname(nickname);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addNewUser(@RequestBody UserRequest userRequest) {
        return userService.addNewUser(userRequest);
    }

    @PatchMapping()
    public UserDto updateNickname(@RequestBody UserRequest request) {
        return userService.updateNickname(request);
    }
}
