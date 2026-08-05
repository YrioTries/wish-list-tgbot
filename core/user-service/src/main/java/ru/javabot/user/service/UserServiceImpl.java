package ru.javabot.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javabot.interaction_api.exeption.NotFoundException;
import ru.javabot.interaction_api.user.dto.UserDto;
import ru.javabot.user.dao.UserDao;
import ru.javabot.user.dao.UserMapper;
import ru.javabot.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto findUserByUsername(String nickname) {
        UserDao user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new NotFoundException("Пользователь с никнеймом " + nickname + " не найден"));

        return userMapper.toDto(user);
    }

    private void checkUserNickname(String nickname) {
        if (!userRepository.existsByNickname(nickname)) {
            throw new NotFoundException("No user with the same nickname");
        }
    }
}
