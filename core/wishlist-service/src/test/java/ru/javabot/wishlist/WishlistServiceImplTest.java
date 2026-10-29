package ru.javabot.wishlist;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.javabot.interaction_api.model.wishlist.dto.AccessRights;
import ru.javabot.interaction_api.model.wishlist.dto.CreateWishlistRequest;
import ru.javabot.interaction_api.model.wishlist.dto.WishlistDto;
import ru.javabot.wishlist.dao.WishlistDao;
import ru.javabot.wishlist.dao.WishlistMapper;
import ru.javabot.wishlist.repository.WishlistRepository;
import ru.javabot.wishlist.service.WishlistServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@Epic("Управление списками желаний")
@ExtendWith(MockitoExtension.class)
class WishlistServiceImplTest {

    @Mock
    private WishlistMapper wishlistMapper;

    @Mock
    private WishlistRepository wishlistRepository;

    @InjectMocks
    private WishlistServiceImpl wishlistService;

    @Captor
    private ArgumentCaptor<WishlistDao> wishlistDaoCaptor;

    @Test
    @Tag("smoke")
    @Feature("Создание списка")
    @Story("Успешное создание списка")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет, что при создании списка устанавливается ownerId, список сохраняется и возвращается DTO")
    @DisplayName("addNewWishlist: устанавливает ownerId, сохраняет wishlist и возвращает DTO")
    void addNewWishlist_shouldSetOwnerIdAndReturnDto() {
        Long ownerId = 10L;

        CreateWishlistRequest request = new CreateWishlistRequest(
                "Мои подарки",
                AccessRights.PUBLIC
        );

        WishlistDao mappedDao = WishlistDao.builder()
                .name("Мои подарки")
                .accessRights(AccessRights.PUBLIC)
                .build();

        WishlistDao savedDao = WishlistDao.builder()
                .id(1L)
                .ownerId(ownerId)
                .name("Мои подарки")
                .accessRights(AccessRights.PUBLIC)
                .build();

        WishlistDto expectedDto = new WishlistDto(
                1L,
                ownerId,
                "Мои подарки",
                AccessRights.PUBLIC
        );

        when(wishlistMapper.toDao(request)).thenReturn(mappedDao);
        when(wishlistRepository.save(mappedDao)).thenReturn(savedDao);
        when(wishlistMapper.toDto(savedDao)).thenReturn(expectedDto);

        WishlistDto actual = wishlistService.addNewWishlist(ownerId, request);

        assertThat(actual).isEqualTo(expectedDto);

        verify(wishlistRepository).save(wishlistDaoCaptor.capture());

        WishlistDao captured = wishlistDaoCaptor.getValue();
        assertThat(captured.getOwnerId()).isEqualTo(ownerId);
        assertThat(captured.getName()).isEqualTo("Мои подарки");
        assertThat(captured.getAccessRights()).isEqualTo(AccessRights.PUBLIC);

        verify(wishlistMapper).toDao(request);
        verify(wishlistMapper).toDto(savedDao);
        verifyNoMoreInteractions(wishlistMapper, wishlistRepository);
    }

    @Test
    @Tag("regression")
    @Feature("Просмотр списков")
    @Story("Успешный просмотр всех списков")
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверяет, что при запросе списков пользователя возвращаются DTO всех его списков")
    @DisplayName("showWishlists: возвращает DTO всех wishlists пользователя")
    void showWishlists_shouldMapAllDaoToDto() {
        Long ownerId = 10L;

        WishlistDao first = WishlistDao.builder()
                .id(1L)
                .ownerId(ownerId)
                .name("Первая")
                .accessRights(AccessRights.PRIVATE)
                .build();

        WishlistDao second = WishlistDao.builder()
                .id(2L)
                .ownerId(ownerId)
                .name("Вторая")
                .accessRights(AccessRights.PUBLIC)
                .build();

        WishlistDto firstDto = new WishlistDto(
                1L, ownerId, "Первая", AccessRights.PRIVATE
        );

        WishlistDto secondDto = new WishlistDto(
                2L, ownerId, "Вторая", AccessRights.PUBLIC
        );

        when(wishlistRepository.findByOwnerId(ownerId))
                .thenReturn(List.of(first, second));
        when(wishlistMapper.toDto(first)).thenReturn(firstDto);
        when(wishlistMapper.toDto(second)).thenReturn(secondDto);

        List<WishlistDto> actual = wishlistService.showWishlists(ownerId);

        assertThat(actual).containsExactly(firstDto, secondDto);

        verify(wishlistRepository).findByOwnerId(ownerId);
        verify(wishlistMapper).toDto(first);
        verify(wishlistMapper).toDto(second);
        verifyNoMoreInteractions(wishlistMapper, wishlistRepository);
    }

    @Test
    @Tag("regression")
    @Feature("Просмотр списков")
    @Story("Просмотр пустого списка")
    @Severity(SeverityLevel.MINOR)
    @Description("Проверяет, что при отсутствии списков у пользователя возвращается пустой список")
    @DisplayName("showWishlists: возвращает пустой список, если wishlists нет")
    void showWishlists_shouldReturnEmptyList() {
        Long ownerId = 10L;

        when(wishlistRepository.findByOwnerId(ownerId)).thenReturn(List.of());

        List<WishlistDto> actual = wishlistService.showWishlists(ownerId);

        assertThat(actual).isEmpty();

        verify(wishlistRepository).findByOwnerId(ownerId);
        verify(wishlistMapper, org.mockito.Mockito.never()).toDto(any());
    }
}