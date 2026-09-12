package ru.javabot.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.javabot.user.dao.UserDao;

import java.util.Optional;

@Repository
public interface UserRepository
        extends JpaRepository<UserDao, Long> {

    Optional<UserDao> findByTelegramChatId(Long chatId);

    Optional<UserDao> findByNickname(String nickname);

    boolean existsByNickname(String nickname);

    boolean existsByNicknameAndIdNot(
            String nickname,
            Long id
    );
}
