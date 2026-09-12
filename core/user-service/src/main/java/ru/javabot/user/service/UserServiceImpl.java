package ru.javabot.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javabot.interaction_api.exeption.BadRequestException;
import ru.javabot.interaction_api.exeption.NotFoundException;
import ru.javabot.interaction_api.user.dto.CreateUserRequest;
import ru.javabot.interaction_api.user.dto.UserDto;
import ru.javabot.user.dao.UserDao;
import ru.javabot.user.dao.UserMapper;
import ru.javabot.user.repository.UserRepository;

import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto findUserByNickname(String nickname) {
        String normalizedNickname = normalizeUsername(nickname);

        if (normalizedNickname == null) {
            throw new BadRequestException(
                    "Никнейм не может быть пустым"
            );
        }

        UserDao user = userRepository
                .findByNickname(normalizedNickname)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Пользователь с никнеймом @" +
                                        normalizedNickname +
                                        " не найден"
                        )
                );

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto addNewUser(CreateUserRequest userRequest) {
        String nickname = normalizeUsername(userRequest.getNickname());

        if (nickname != null &&
                userRepository.existsByNickname(nickname)) {
            throw new BadRequestException(
                    "Пользователь с @" + nickname +
                            " уже существует"
            );
        }

        UserDao userDao = userMapper.toDao(userRequest);
        userDao.setNickname(nickname);

        return userMapper.toDto(
                userRepository.save(userDao)
        );
    }

    @Override
    @Transactional
    public UserDto updateNickname(
            Long chatId,
            String newNickname
    ) {
        UserDao userDao = userRepository
                .findByTelegramChatId(chatId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Пользователь с chatId " +
                                        chatId +
                                        " не найден"
                        )
                );

        String normalizedNickname =
                normalizeUsername(newNickname);

        if (normalizedNickname == null) {
            throw new BadRequestException(
                    "Никнейм не может быть пустым"
            );
        }

        boolean nicknameTaken =
                userRepository.existsByNicknameAndIdNot(
                        normalizedNickname,
                        userDao.getId()
                );

        if (nicknameTaken) {
            throw new BadRequestException(
                    "Никнейм @" + normalizedNickname +
                            " уже занят"
            );
        }

        userDao.setNickname(normalizedNickname);

        return userMapper.toDto(
                userRepository.save(userDao)
        );
    }

    @Transactional
    public UserDto synchronizeTelegramUser(Long chatId, String telegramUsername) {
        String newUsername =
                normalizeUsername(telegramUsername);

        UserDao currentUser = userRepository
                .findByTelegramChatId(chatId)
                .orElseGet(() -> {
                    UserDao user = new UserDao();
                    user.setTelegramChatId(chatId);
                    return user;
                });

        if (Objects.equals(
                currentUser.getNickname(),
                newUsername
        )) {
            return userMapper.toDto(currentUser);
        }

        if (newUsername != null) {
            userRepository.findByNickname(newUsername)
                    .filter(existingUser ->
                            !Objects.equals(
                                    existingUser.getTelegramChatId(),
                                    chatId
                            )
                    )
                    .ifPresent(existingUser -> {
                        existingUser.setNickname(null);
                        userRepository.saveAndFlush(existingUser);
                    });
        }

        currentUser.setNickname(newUsername);

        return userMapper.toDto(
                userRepository.save(currentUser)
        );
    }

    private String normalizeUsername(String username) {
        if (username == null || username.isBlank()) {
            return null;
        }

        String normalized = username.trim();

        if (normalized.startsWith("@")) {
            normalized = normalized.substring(1);
        }

        return normalized.toLowerCase(Locale.ROOT);
    }
}
