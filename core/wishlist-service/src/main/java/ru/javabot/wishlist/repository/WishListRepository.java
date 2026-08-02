package ru.javabot.wishlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javabot.wishlist.dao.WishListDao;

public interface WishListRepository extends JpaRepository<WishListDao, Long> {
}
