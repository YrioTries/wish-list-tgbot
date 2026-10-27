package ru.javabot.interaction_api.model.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.javabot.interaction_api.model.user.dto.UserRequest;
import ru.javabot.interaction_api.model.user.dto.UserDto;

@FeignClient(
        name = "user-service",
        path = "/users"
)
public interface UserClient {
    @GetMapping
    UserDto findUserByNickname(@RequestParam("nickname") String nickname);

    @PostMapping
    UserDto addNewUser(@RequestBody UserRequest userRequest);

    @PatchMapping
    UserDto updateNickname(@RequestBody UserRequest request);
}
