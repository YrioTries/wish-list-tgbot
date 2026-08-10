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
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto findUserByUsername(String nickname) {
        UserDao user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new NotFoundException("Пользователь с никнеймом " + nickname + " не найден"));

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto addNewUser(CreateUserRequest userRequest) {
        if (userRepository.existsByNickname(userRequest.getNickname())) {
            throw new BadRequestException("User with @" + userRequest.getNickname() + " already exists");
        }
        UserDao userDao = userMapper.toDao(userRequest);
        return userMapper.toDto(userRepository.save(userDao));
    }

    @Override
    @Transactional
    public UserDto updateNickname(Long chatId, String newNickname) {
        UserDao userDao = userRepository.findByTelegramChatId(chatId)
                .orElseThrow(() -> new NotFoundException("User with chatId " + chatId + " not found"));


        boolean nicknameTaken = userRepository.existsByNickname(newNickname);

        boolean nicknameBelongsToCurrentUser =
                Objects.equals(
                        userDao.getNickname(),
                        newNickname
                );

        if (nicknameTaken && !nicknameBelongsToCurrentUser) {
            throw new BadRequestException(
                    "Nickname @" + newNickname + " is already taken"
            );
        }

        userDao.setNickname(newNickname);
        return userMapper.toDto(userRepository.save(userDao));
    }

    @Transactional
    public UserDto synchronizeTelegramUser(
            Long chatId,
            String telegramUsername
    ) {
        String newUsername = normalizeUsername(telegramUsername);

        UserDao currentUser = userRepository
                .findByTelegramChatId(chatId)
                .orElseGet(() -> {
                    UserDao user = new UserDao();
                    user.setTelegramChatId(chatId);
                    return user;
                });

        /*
         * Если username не изменился,
         * никаких дополнительных действий не требуется.
         */
        if (Objects.equals(currentUser.getNickname(), newUsername)) {
            return userMapper.toDto(currentUser);
        }

        /*
         * Если Telegram прислал нового username,
         * проверяем, не осталась ли такая запись
         * за другим chatId.
         */
        if (newUsername != null) {
            userRepository.findByNickname(newUsername)
                    .filter(existingUser ->
                            !Objects.equals(
                                    existingUser.getTelegramChatId(),
                                    chatId
                            )
                    )
                    .ifPresent(existingUser -> {
                        /*
                         * Освобождаем username у старого пользователя.
                         */
                        existingUser.setNickname(null);

                        /*
                         * Сначала фиксируем освобождение username
                         * в базе данных.
                         */
                        userRepository.saveAndFlush(existingUser);
                    });
        }
        /*
         * Если newUsername == null,
         * старый username будет удалён у текущего пользователя.

         * Если newUsername содержит значение,
         * он будет закреплён за текущим пользователем.
         */
        currentUser.setNickname(newUsername);

        UserDao savedUser = userRepository.save(currentUser);

        return userMapper.toDto(savedUser);
    }
    private String normalizeUsername(String username) {
        if (username == null || username.isBlank()) {
            return null;
        }

        return username
                .trim()
                .replaceFirst("^@", "")
                .toLowerCase(Locale.ROOT);
    }

}
