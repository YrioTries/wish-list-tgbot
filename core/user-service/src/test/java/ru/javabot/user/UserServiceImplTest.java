package ru.javabot.user;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.javabot.interaction_api.exeption.BadRequestException;
import ru.javabot.interaction_api.exeption.NotFoundException;
import ru.javabot.interaction_api.model.user.dto.UserRequest;
import ru.javabot.interaction_api.model.user.dto.UserDto;
import ru.javabot.user.dao.UserDao;
import ru.javabot.user.dao.UserMapper;
import ru.javabot.user.repository.UserRepository;
import ru.javabot.user.service.UserServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<UserDao> userDaoCaptor;

    @Test
    @Feature("Поиск пользователя")
    @Story("Успешный поиск по никнейму")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет, что сервис возвращает DTO пользователя, если он найден по никнейму")
    @DisplayName("Находит пользователя по нормализованному никнейму")
    void findUserByNickname_shouldReturnUser_whenUserExists() {
        String nickname = "dmitriy";

        UserDao userDao = UserDao.builder()
                .id(1L)
                .telegramChatId(100L)
                .nickname(nickname)
                .build();

        UserDto expectedDto = new UserDto(1L, 100L, nickname);

        when(userRepository.findByNickname(nickname))
                .thenReturn(Optional.of(userDao));
        when(userMapper.toDto(userDao)).thenReturn(expectedDto);

        UserDto result = userService.findUserByNickname(nickname);

        assertThat(result).isEqualTo(expectedDto);

        verify(userRepository).findByNickname(nickname);
        verify(userMapper).toDto(userDao);
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    @Feature("Поиск пользователя")
    @Story("Ошибка: пользователь не найден")
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверяет, что сервис бросает NotFoundException, если пользователь не найден")
    @DisplayName("Бросает NotFoundException, если пользователь не найден")
    void findUserByNickname_shouldThrowException_whenUserDoesNotExist() {
        when(userRepository.findByNickname("unknown"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findUserByNickname("unknown"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Пользователь с никнеймом @unknown не найден");

        verify(userMapper, never()).toDto(any(UserDao.class));
    }

    @Test
    @Feature("Регистрация пользователя")
    @Story("Успешная регистрация")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет, что сервис создаёт нового пользователя, если никнейм свободен")
    @DisplayName("Создаёт пользователя, если никнейм свободен")
    void addNewUser_shouldCreateUser_whenNicknameIsFree() {
        UserRequest request = new UserRequest(100L, "dmitriy");

        UserDao userDao = new UserDao();
        userDao.setTelegramChatId(100L);

        UserDao savedUserDao = UserDao.builder()
                .id(1L)
                .telegramChatId(100L)
                .nickname("dmitriy")
                .build();

        UserDto expectedDto = new UserDto(1L, 100L, "dmitriy");

        when(userRepository.existsByNickname("dmitriy")).thenReturn(false);
        when(userMapper.toDao(request)).thenReturn(userDao);
        when(userRepository.save(userDao)).thenReturn(savedUserDao);
        when(userMapper.toDto(savedUserDao)).thenReturn(expectedDto);

        UserDto result = userService.addNewUser(request);

        assertThat(result).isEqualTo(expectedDto);

        verify(userRepository).save(userDaoCaptor.capture());

        assertThat(userDaoCaptor.getValue().getNickname())
                .isEqualTo("dmitriy");
    }

    @Test
    @Feature("Регистрация пользователя")
    @Story("Ошибка: никнейм уже занят")
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверяет, что сервис бросает BadRequestException, если никнейм уже существует")
    @DisplayName("Бросает BadRequestException, если никнейм уже существует")
    void addNewUser_shouldThrowException_whenNicknameAlreadyExists() {
        UserRequest request = new UserRequest(100L, "dmitriy");

        when(userRepository.existsByNickname("dmitriy")).thenReturn(true);

        assertThatThrownBy(() -> userService.addNewUser(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Пользователь с @dmitriy уже существует");

        verify(userMapper, never()).toDao(any(UserRequest.class));
        verify(userRepository, never()).save(any(UserDao.class));
        verify(userMapper, never()).toDto(any(UserDao.class));
    }

    @Test
    @Feature("Обновление никнейма")
    @Story("Успешное обновление")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет, что сервис обновляет никнейм, если новый никнейм свободен")
    @DisplayName("Обновляет никнейм, если новый никнейм свободен")
    void updateNickname_shouldUpdateNickname_whenNewNicknameIsFree() {
        UserRequest userRequest = new UserRequest(100L, "new_name");

        UserDao userDao = UserDao.builder()
                .id(1L)
                .telegramChatId(userRequest.getTelegramChatId())
                .nickname("old_name")
                .build();

        UserDto expectedDto = new UserDto(1L, userRequest.getTelegramChatId(), userRequest.getNickname());

        when(userRepository.findByTelegramChatId(userRequest.getTelegramChatId()))
                .thenReturn(Optional.of(userDao));

        when(userRepository.existsByNicknameAndIdNot(userRequest.getNickname(), 1L))
                .thenReturn(false);

        when(userRepository.save(userDao)).thenReturn(userDao);
        when(userMapper.toDto(userDao)).thenReturn(expectedDto);

        UserDto result = userService.updateNickname(userRequest);

        assertThat(result).isEqualTo(expectedDto);
        assertThat(userDao.getNickname()).isEqualTo(userRequest.getNickname());

        verify(userRepository).existsByNicknameAndIdNot(userRequest.getNickname(), 1L);
        verify(userRepository).save(userDao);
        verify(userMapper).toDto(userDao);
    }

    @Test
    @Feature("Обновление никнейма")
    @Story("Ошибка: никнейм занят")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Бросает BadRequestException, если новый никнейм занят другим пользователем")
    void updateNickname_shouldThrowException_whenNewNicknameIsTakenByAnotherUser() {
        Long chatId = 100L;
        UserRequest request = new UserRequest(chatId, "taken_name");

        UserDao userDao = UserDao.builder()
                .id(1L)
                .telegramChatId(chatId)
                .nickname("old_name")
                .build();

        when(userRepository.findByTelegramChatId(chatId))
                .thenReturn(Optional.of(userDao));

        when(userRepository.existsByNicknameAndIdNot("taken_name", 1L))
                .thenReturn(true);

        assertThatThrownBy(() ->
                userService.updateNickname(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Никнейм @taken_name уже занят");

        assertThat(userDao.getNickname()).isEqualTo("old_name");

        verify(userRepository, never()).save(any(UserDao.class));
        verify(userMapper, never()).toDto(any(UserDao.class));
    }

    @Test
    @Feature("Обновление никнейма")
    @Story("Ошибка: пользователь не найден")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Бросает NotFoundException, если пользователь не найден")
    void updateNickname_shouldThrowException_whenUserDoesNotExist() {
        UserRequest request = new UserRequest(999L, "new_name");
        when(userRepository.findByTelegramChatId(999L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                userService.updateNickname(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Пользователь с chatId 999 не найден");

        verify(userRepository, never())
                .existsByNicknameAndIdNot(anyString(), any());
        verify(userRepository, never()).save(any(UserDao.class));
        verify(userMapper, never()).toDto(any(UserDao.class));
    }
}