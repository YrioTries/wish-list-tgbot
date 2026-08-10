package ru.javabot.wish;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.javabot.interaction_api.wish.dto.CreateWishRequest;
import ru.javabot.interaction_api.wish.dto.WishDto;
import ru.javabot.interaction_api.wish.dto.WishStatus;
import ru.javabot.wish.dao.ReferenceDao;
import ru.javabot.wish.dao.WishDao;
import ru.javabot.wish.dao.WishMapper;
import ru.javabot.wish.repository.ReferenceRepository;
import ru.javabot.wish.repository.WishRepository;
import ru.javabot.wish.service.WishServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

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

    @Test
    void createWishes_shouldCreateWishesWithNewReferences() {
        Long wishlistId = 1L;
        List<CreateWishRequest> requests = List.of(
                new CreateWishRequest("Wish 1", "ref1", 100L, "Desc 1"),
                new CreateWishRequest("Wish 2", "ref2", 200L, "Desc 2")
        );

        when(referenceRepository.findByReferenceIn(anyList()))
                .thenReturn(List.of());

        List<ReferenceDao> createdRefs = List.of(
                ReferenceDao.builder().id(1L).reference("ref1").build(),
                ReferenceDao.builder().id(2L).reference("ref2").build()
        );
        when(referenceRepository.saveAll(anyList())).thenReturn(createdRefs);

        // Mapper: toDao
        List<WishDao> wishDaoList = List.of(
                WishDao.builder().name("Wish 1").build(),
                WishDao.builder().name("Wish 2").build()
        );
        when(wishMapper.toDao(requests.get(0))).thenReturn(wishDaoList.get(0));
        when(wishMapper.toDao(requests.get(1))).thenReturn(wishDaoList.get(1));

        List<WishDao> savedWishes = List.of(
                WishDao.builder().id(1L).name("Wish 1").wishlistId(wishlistId)
                        .status(WishStatus.AVAILABLE).expectedPrice(100L).description("Desc 1")
                        .reference(createdRefs.get(0)).build(),
                WishDao.builder().id(2L).name("Wish 2").wishlistId(wishlistId)
                        .status(WishStatus.AVAILABLE).expectedPrice(200L).description("Desc 2")
                        .reference(createdRefs.get(1)).build()
        );
        when(wishRepository.saveAll(anyList())).thenReturn(savedWishes);

        List<WishDto> wishDtoList = List.of(
                new WishDto(1L, wishlistId, "Wish 1", 1L, WishStatus.AVAILABLE, 100L, "Desc 1"),
                new WishDto(2L, wishlistId, "Wish 2", 2L, WishStatus.AVAILABLE, 200L, "Desc 2")
        );
        when(wishMapper.toDto(savedWishes.get(0))).thenReturn(wishDtoList.get(0));
        when(wishMapper.toDto(savedWishes.get(1))).thenReturn(wishDtoList.get(1));

        List<WishDto> result = wishService.createWishes(wishlistId, requests);

        assertEquals(2, result.size());
        assertEquals("Wish 1", result.getFirst().getName());
        assertEquals(1L, result.getFirst().getReferenceId());

        verify(referenceRepository).findByReferenceIn(anyList());
        verify(referenceRepository).saveAll(anyList());
        verify(wishRepository).saveAll(anyList());
    }

    @Test
    void createWishes_shouldReuseExistingReferences() {
        Long wishlistId = 1L;
        List<CreateWishRequest> requests = List.of(
                new CreateWishRequest("Wish 1", "ref1", 100L, "Desc 1")
        );

        List<ReferenceDao> existingRefs = List.of(
                ReferenceDao.builder().id(99L).reference("ref1").build()
        );
        when(referenceRepository.findByReferenceIn(anyList()))
                .thenReturn(existingRefs);

        WishDao wishDao = WishDao.builder().name("Wish 1").build();
        when(wishMapper.toDao(requests.getFirst())).thenReturn(wishDao);

        List<WishDao> savedWishes = List.of(
                WishDao.builder().id(1L).name("Wish 1").wishlistId(wishlistId)
                        .status(WishStatus.AVAILABLE).expectedPrice(100L).description("Desc 1")
                        .reference(existingRefs.getFirst()).build()
        );
        when(wishRepository.saveAll(anyList())).thenReturn(savedWishes);

        WishDto wishDto = new WishDto(1L, wishlistId, "Wish 1", 99L, WishStatus.AVAILABLE, 100L, "Desc 1");
        when(wishMapper.toDto(savedWishes.getFirst())).thenReturn(wishDto);

        List<WishDto> result = wishService.createWishes(wishlistId, requests);

        assertEquals(1, result.size());
        assertEquals(99L, result.getFirst().getReferenceId());
        verify(referenceRepository, never()).saveAll(anyList());
    }
}
