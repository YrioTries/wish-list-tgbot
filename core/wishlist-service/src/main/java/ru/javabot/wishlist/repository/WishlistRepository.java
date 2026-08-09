package ru.javabot.wishlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javabot.wishlist.dao.WishlistDao;

public interface WishlistRepository extends JpaRepository<WishlistDao, Long> {
}
