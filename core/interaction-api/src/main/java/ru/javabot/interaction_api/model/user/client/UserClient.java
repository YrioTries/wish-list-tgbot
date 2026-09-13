package ru.javabot.interaction_api.model.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javabot.interaction_api.model.user.dto.CreateUserRequest;
import ru.javabot.interaction_api.model.user.dto.UserDto;

@FeignClient(name = "user-service", path = "/users")
public interface UserClient {
    @GetMapping
    UserDto findUserByNickname(String nickname);

    @PostMapping
    UserDto addNewUser(CreateUserRequest userRequest);

    @PatchMapping
    UserDto updateNickname(Long chatId, String newNickname);
}
