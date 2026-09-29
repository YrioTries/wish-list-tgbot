package ru.javabot.wishlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javabot.wishlist.dao.WishlistDao;

import java.util.List;

public interface WishlistRepository extends JpaRepository<WishlistDao, Long> {
    List<WishlistDao> findByOwnerId(Long ownerId);
}
