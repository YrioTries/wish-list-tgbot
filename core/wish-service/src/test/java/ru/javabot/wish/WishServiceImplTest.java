package ru.javabot.wish;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.javabot.interaction_api.model.wish.dto.CreateWishRequest;
import ru.javabot.interaction_api.model.wish.dto.WishDto;
import ru.javabot.interaction_api.model.wish.dto.WishStatus;
import ru.javabot.wish.dao.ReferenceDao;
import ru.javabot.wish.dao.WishDao;
import ru.javabot.wish.dao.WishMapper;
import ru.javabot.wish.repository.ReferenceRepository;
import ru.javabot.wish.repository.WishRepository;
import ru.javabot.wish.service.WishServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@Epic("Управление желаниями")
@ExtendWith(MockitoExtension.class)
class WishServiceImplTest {

    @Mock
    private WishRepository wishRepository;

    @Mock
    private ReferenceRepository referenceRepository;

    @Mock
    private WishMapper wishMapper;

    @InjectMocks
    private WishServiceImpl wishService;

    @Captor
    private ArgumentCaptor<List<WishDao>> wishesCaptor;

    @Captor
    private ArgumentCaptor<List<ReferenceDao>> referencesCaptor;

    @Test
    @Feature("Создание желаний")
    @Story("Создание с новыми справочниками")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверяет, что при создании желаний создаются новые справочники и корректно связываются с желаниями")
    @DisplayName("Создаёт wishes, новые references и связывает их корректно")
    void createWishes_shouldCreateWishesWithNewReferences() {
        Long wishlistId = 1L;

        CreateWishRequest firstRequest =
                new CreateWishRequest("Wish 1", "ref1", 100L, "Desc 1");
        CreateWishRequest secondRequest =
                new CreateWishRequest("Wish 2", "ref2", 200L, "Desc 2");

        List<CreateWishRequest> requests =
                List.of(firstRequest, secondRequest);

        when(referenceRepository.findByReferenceIn(List.of("ref1", "ref2")))
                .thenReturn(List.of());

        ReferenceDao firstReference = ReferenceDao.builder()
                .id(1L)
                .reference("ref1")
                .build();

        ReferenceDao secondReference = ReferenceDao.builder()
                .id(2L)
                .reference("ref2")
                .build();

        when(referenceRepository.saveAll(anyList()))
                .thenReturn(List.of(firstReference, secondReference));

        WishDao firstWish = new WishDao();
        firstWish.setName("Wish 1");

        WishDao secondWish = new WishDao();
        secondWish.setName("Wish 2");

        when(wishMapper.toDao(firstRequest)).thenReturn(firstWish);
        when(wishMapper.toDao(secondRequest)).thenReturn(secondWish);

        when(wishRepository.saveAll(anyList()))
                .thenReturn(List.of(firstWish, secondWish));

        WishDto firstDto = new WishDto(
                1L, wishlistId, "Wish 1",
                1L, WishStatus.AVAILABLE, 100L, "Desc 1"
        );

        WishDto secondDto = new WishDto(
                2L, wishlistId, "Wish 2",
                2L, WishStatus.AVAILABLE, 200L, "Desc 2"
        );

        when(wishMapper.toDto(firstWish)).thenReturn(firstDto);
        when(wishMapper.toDto(secondWish)).thenReturn(secondDto);

        List<WishDto> result = wishService.createWishes(wishlistId, requests);

        assertThat(result).containsExactly(firstDto, secondDto);

        verify(referenceRepository).saveAll(referencesCaptor.capture());

        assertThat(referencesCaptor.getValue())
                .extracting(ReferenceDao::getReference)
                .containsExactly("ref1", "ref2");

        verify(wishRepository).saveAll(wishesCaptor.capture());

        List<WishDao> savedWishes = wishesCaptor.getValue();

        assertThat(savedWishes).hasSize(2);

        WishDao savedFirst = savedWishes.get(0);
        WishDao savedSecond = savedWishes.get(1);

        assertThat(savedFirst.getWishlistId()).isEqualTo(wishlistId);
        assertThat(savedFirst.getStatus()).isEqualTo(WishStatus.AVAILABLE);
        assertThat(savedFirst.getReference()).isEqualTo(firstReference);

        assertThat(savedSecond.getWishlistId()).isEqualTo(wishlistId);
        assertThat(savedSecond.getStatus()).isEqualTo(WishStatus.AVAILABLE);
        assertThat(savedSecond.getReference()).isEqualTo(secondReference);

        verifyNoMoreInteractions(
                wishRepository,
                referenceRepository,
                wishMapper
        );
    }

    @Test
    @Feature("Создание желаний")
    @Story("Переиспользование существующих справочников")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Переиспользует существующую reference и не создаёт новую")
    void createWishes_shouldReuseExistingReferences() {
        Long wishlistId = 1L;

        CreateWishRequest request =
                new CreateWishRequest("Wish 1", "ref1", 100L, "Desc 1");

        ReferenceDao existingReference = ReferenceDao.builder()
                .id(99L)
                .reference("ref1")
                .build();

        when(referenceRepository.findByReferenceIn(List.of("ref1")))
                .thenReturn(List.of(existingReference));

        WishDao wishDao = new WishDao();
        wishDao.setName("Wish 1");

        when(wishMapper.toDao(request)).thenReturn(wishDao);

        when(wishRepository.saveAll(List.of(wishDao)))
                .thenReturn(List.of(wishDao));

        WishDto wishDto = new WishDto(
                1L, wishlistId, "Wish 1",
                99L, WishStatus.AVAILABLE, 100L, "Desc 1"
        );

        when(wishMapper.toDto(wishDao)).thenReturn(wishDto);

        List<WishDto> result =
                wishService.createWishes(wishlistId, List.of(request));

        assertThat(result).containsExactly(wishDto);

        verify(referenceRepository, never()).saveAll(anyList());

        verify(wishRepository).saveAll(wishesCaptor.capture());

        WishDao savedWish = wishesCaptor.getValue().getFirst();

        assertThat(savedWish.getWishlistId()).isEqualTo(wishlistId);
        assertThat(savedWish.getStatus()).isEqualTo(WishStatus.AVAILABLE);
        assertThat(savedWish.getReference()).isEqualTo(existingReference);
    }
}