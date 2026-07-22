package ru.javabot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javabot.dao.Wish;

public interface WishListRepository extends JpaRepository<Wish, Long> {
}
