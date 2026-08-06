package ru.javabot.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.javabot.user.dao.UserDao;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDao, Long> {

    Optional<UserDao> findByNickname(String nickname);

    Optional<UserDao> findByTelegramChatId(Long telegramChatId);

    boolean existsByNickname(String nickname);

    boolean existsByTelegramChatId(Long telegramChatId);
}
