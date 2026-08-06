package ru.javabot.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javabot.interaction_api.exeption.BadRequestException;
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

    @Override
    public UserDto findUserByUsername(String nickname) {
        UserDao user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new NotFoundException("Пользователь с никнеймом " + nickname + " не найден"));

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto addNewUser(UserDto userDto) {
        if (userRepository.existsByNickname(userDto.getNickname())) {
            throw new BadRequestException("User with @" + userDto.getNickname() + " already exists");
        }
        UserDao userDao = userMapper.toDao(userDto);
        userRepository.save(userDao);
        return userMapper.toDto(userRepository.save(userDao));
    }

    @Override
    @Transactional
    public UserDto updateNickname(Long chatId, String newNickname) {
        UserDao userDao = userRepository.findByTelegramChatId(chatId)
                .orElseThrow(() -> new NotFoundException("User with chatId " + chatId + " not found"));

        if (userRepository.existsByNickname(newNickname) &&
                !userDao.getNickname().equals(newNickname)) {
            throw new BadRequestException("Nickname @" + newNickname + " is already taken");
        }

        userDao.setNickname(newNickname);
        return userMapper.toDto(userRepository.save(userDao));
    }

    private void checkUserNickname(String nickname) {
        if (!userRepository.existsByNickname(nickname)) {
            throw new NotFoundException("No user with the same nickname");
        }
    }
}
