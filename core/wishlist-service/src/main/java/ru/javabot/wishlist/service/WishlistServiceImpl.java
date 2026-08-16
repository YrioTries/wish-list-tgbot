package ru.javabot.wishlist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javabot.interaction_api.wishlist.dto.CreateWishlistRequest;
import ru.javabot.interaction_api.wishlist.dto.WishlistDto;
import ru.javabot.wishlist.dao.WishlistDao;
import ru.javabot.wishlist.dao.WishlistMapper;
import ru.javabot.wishlist.repository.WishlistRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishlistServiceImpl implements WishlistService{
    private final WishlistMapper wishlistMapper;
    private final WishlistRepository wishlistRepository;

    @Transactional
    public WishlistDto addNewWishlist(Long ownerId, CreateWishlistRequest wishlistRequest) {
        WishlistDao wishlistDao = wishlistMapper.toDao(wishlistRequest);
        wishlistDao.setOwnerId(ownerId);
        WishlistDao wishlistDaoUpdated = wishlistRepository.save(wishlistDao);
        return wishlistMapper.toDto(wishlistDaoUpdated);
    }

    public List<WishlistDto> showWishlists(Long ownerId) {
        List<WishlistDao> wishlistDaoList = wishlistRepository.findByOwnerId(ownerId);

        return wishlistDaoList.stream()
                .map(wishlistMapper::toDto)
                .toList();
    }
}
