package ru.javabot.wish.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javabot.wish.dao.WishDao;

public interface WishRepository extends JpaRepository<WishDao, Long> {
}
