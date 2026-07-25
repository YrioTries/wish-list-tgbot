package ru.javabot.user.dao;

import jakarta.persistence.*;

@Entity
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(columnNames = "telegram_user_id")
)
public class UserDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "telegram_user_id", nullable = false, unique = true)
    private Long userChatId;

    @Column(name = "nickname", length = 50)
    private String nickname;
}
