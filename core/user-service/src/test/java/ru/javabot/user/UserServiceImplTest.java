package ru.javabot.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.javabot.interaction_api.exeption.BadRequestException;
import ru.javabot.interaction_api.exeption.NotFoundException;
import ru.javabot.interaction_api.user.dto.CreateUserRequest;
import ru.javabot.interaction_api.user.dto.UserDto;
import ru.javabot.user.dao.UserDao;
import ru.javabot.user.dao.UserMapper;
import ru.javabot.user.repository.UserRepository;
import ru.javabot.user.service.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findUserByUsername_shouldReturnUser_whenUserExists() {
        // Arrange
        String nickname = "dmitriy";

        UserDao userDao = UserDao.builder()
                .id(1L)
                .telegramChatId(100L)
                .nickname(nickname)
                .build();

        UserDto expectedDto = new UserDto(
                1L,
                100L,
                nickname
        );

    /*
     * Настраиваем мок репозитория.

     * Когда сервис выполнит поиск по никнейму "dmitriy",
       репозиторий должен вернуть найденного пользователя.
    */
        when(userRepository.findByNickname(nickname))
                .thenReturn(Optional.of(userDao));

    /*
     * Настраиваем мок маппера.

     * Когда сервис передаст найденный UserDao в маппер,
       маппер должен вернуть подготовленный UserDto.
    */
        when(userMapper.toDto(userDao))
                .thenReturn(expectedDto);

        // Act
        UserDto result =
                userService.findUserByNickname(nickname);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100L, result.getTelegramChatId());
        assertEquals(nickname, result.getNickname());

        /*
         * Проверяем, что репозиторий вызван с нужным никнеймом.
         */
        verify(userRepository)
                .findByNickname(nickname);

        /*
         * Проверяем, что найденный UserDao преобразован в DTO.
         */
        verify(userMapper)
                .toDto(userDao);
    }

    @Test
    void findUserByUsername_shouldThrowException_whenUserDoesNotExist() {
        // Arrange
        String nickname = "unknown";

        /*
         * Имитируем ситуацию, когда пользователь
         * отсутствует в базе данных.
         */
        when(userRepository.findByNickname(nickname))
                .thenReturn(Optional.empty());

        // Act
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userService.findUserByNickname(nickname)
        );

        // Assert
        assertEquals(
                "Пользователь с никнеймом " + nickname + " не найден",
                exception.getMessage()
        );

    /*
     * Если пользователь не найден,
       маппер не должен вызываться.
    */
        verify(userMapper, never())
                .toDto(any(UserDao.class));
    }

    @Test
    void addNewUser_shouldCreateUser_whenNicknameIsFree() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest(
                100L,
                "dmitriy"
        );

        UserDao userDao = UserDao.builder()
                .telegramChatId(100L)
                .nickname("dmitriy")
                .build();

        UserDao savedUserDao = UserDao.builder()
                .id(1L)
                .telegramChatId(100L)
                .nickname("dmitriy")
                .build();

        UserDto resultDto = new UserDto(
                1L,
                100L,
                "dmitriy"
        );

        /*
         * Никнейм свободен, поэтому пользователь
         * может быть добавлен.
         */
        when(userRepository.existsByNickname("dmitriy"))
                .thenReturn(false);

        /*
         * Преобразуем входной CreateUserRequest
         * в объект UserDao.
         */
        when(userMapper.toDao(request))
                .thenReturn(userDao);

    /*
     * Имитируем сохранение пользователя.

     * Репозиторий возвращает объект уже с ID,
       который был сгенерирован базой данных.
    */
        when(userRepository.save(userDao))
                .thenReturn(savedUserDao);

        /*
         * Преобразуем сохранённый UserDao в UserDto.
         */
        when(userMapper.toDto(savedUserDao))
                .thenReturn(resultDto);

        // Act
        UserDto result =
                userService.addNewUser(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100L, result.getTelegramChatId());
        assertEquals("dmitriy", result.getNickname());

        /*
         * Проверяем проверку существования никнейма.
         */
        verify(userRepository)
                .existsByNickname("dmitriy");

        /*
         * Проверяем преобразование запроса в UserDao.
         */
        verify(userMapper)
                .toDao(request);

        /*
         * Проверяем сохранение пользователя.
         */
        verify(userRepository)
                .save(userDao);

        /*
         * Проверяем преобразование сохранённого пользователя в DTO.
         */
        verify(userMapper)
                .toDto(savedUserDao);
    }

    @Test
    void addNewUser_shouldThrowException_whenNicknameAlreadyExists() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest(
                100L,
                "dmitriy"
        );

        /*
         * Имитируем ситуацию, когда пользователь
         * с таким никнеймом уже есть в базе данных.
         */
        when(userRepository.existsByNickname("dmitriy"))
                .thenReturn(true);

        // Act
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> userService.addNewUser(request)
        );

        // Assert
        assertEquals(
                "User with @dmitriy already exists",
                exception.getMessage()
        );

        /*
         * Если никнейм занят, новый пользователь
         * не должен преобразовываться и сохраняться.
         */
        verify(userMapper, never())
                .toDao(any(CreateUserRequest.class));

        verify(userRepository, never())
                .save(any(UserDao.class));

        verify(userMapper, never())
                .toDto(any(UserDao.class));
    }

    @Test
    void updateNickname_shouldUpdateNickname_whenNewNicknameIsFree() {
        // Arrange
        Long chatId = 100L;
        String newNickname = "new_name";

        UserDao userDao = UserDao.builder()
                .id(1L)
                .telegramChatId(chatId)
                .nickname("old_name")
                .build();

        UserDao savedUserDao = UserDao.builder()
                .id(1L)
                .telegramChatId(chatId)
                .nickname(newNickname)
                .build();

        UserDto resultDto = new UserDto(
                1L,
                chatId,
                newNickname
        );

        /*
         * Находим пользователя по Telegram chat ID.
         */
        when(userRepository.findByTelegramChatId(chatId))
                .thenReturn(Optional.of(userDao));

    /*
     * Проверяем новый никнейм.

     * false означает, что новый никнейм
       ещё никем не занят.
    */
        when(userRepository.existsByNickname(newNickname))
                .thenReturn(false);

        /*
         * Имитируем сохранение изменённого пользователя.
         */
        when(userRepository.save(userDao))
                .thenReturn(savedUserDao);

        /*
         * Преобразуем сохранённого пользователя в DTO.
         */
        when(userMapper.toDto(savedUserDao))
                .thenReturn(resultDto);

        // Act
        UserDto result =
                userService.updateNickname(chatId, newNickname);

        // Assert
        assertNotNull(result);
        assertEquals(newNickname, result.getNickname());

    /*
     * Проверяем, что объект UserDao действительно
       получил новый никнейм до сохранения.
    */
        assertEquals(newNickname, userDao.getNickname());

        /*
         * Проверяем поиск пользователя.
         */
        verify(userRepository)
                .findByTelegramChatId(chatId);

        /*
         * Проверяем проверку занятости никнейма.
         */
        verify(userRepository)
                .existsByNickname(newNickname);

        /*
         * Проверяем сохранение изменённого пользователя.
         */
        verify(userRepository)
                .save(userDao);

        /*
         * Проверяем преобразование результата в DTO.
         */
        verify(userMapper)
                .toDto(savedUserDao);
    }

    @Test
    void updateNickname_shouldThrowException_whenNewNicknameIsTakenByAnotherUser() {
        // Arrange
        Long chatId = 100L;
        String oldNickname = "old_name";
        String newNickname = "taken_name";

        UserDao userDao = UserDao.builder()
                .id(1L)
                .telegramChatId(chatId)
                .nickname(oldNickname)
                .build();

        /*
         * Находим текущего пользователя.
         */
        when(userRepository.findByTelegramChatId(chatId))
                .thenReturn(Optional.of(userDao));

        /*
         * Новый никнейм уже занят.
         */
        when(userRepository.existsByNickname(newNickname))
                .thenReturn(true);

        // Act
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> userService.updateNickname(chatId, newNickname)
        );

        // Assert
        assertEquals(
                "Nickname @" + newNickname + " is already taken",
                exception.getMessage()
        );

        /*
         * Старый никнейм не должен измениться.
         */
        assertEquals(oldNickname, userDao.getNickname());

    /*
     * Пользователь не должен сохраняться,
       если новый никнейм занят.
    */
        verify(userRepository, never())
                .save(any(UserDao.class));

        verify(userMapper, never())
                .toDto(any(UserDao.class));
    }

    @Test
    void updateNickname_shouldThrowException_whenUserDoesNotExist() {
        // Arrange
        Long chatId = 999L;
        String newNickname = "new_name";

        /*
         * Пользователь с таким chatId отсутствует.
         */
        when(userRepository.findByTelegramChatId(chatId))
                .thenReturn(Optional.empty());

        // Act
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userService.updateNickname(chatId, newNickname)
        );

        // Assert
        assertEquals(
                "User with chatId " + chatId + " not found",
                exception.getMessage()
        );

    /*
     * Если пользователь не найден,
       остальные операции выполняться не должны.
    */
        verify(userRepository, never())
                .existsByNickname(anyString());

        verify(userRepository, never())
                .save(any(UserDao.class));

        verify(userMapper, never())
                .toDto(any(UserDao.class));
    }
}
