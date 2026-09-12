package ru.javabot.wish.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javabot.interaction_api.wish.dto.CreateWishRequest;
import ru.javabot.interaction_api.wish.dto.WishDto;
import ru.javabot.wish.dao.ReferenceDao;
import ru.javabot.wish.dao.WishDao;
import ru.javabot.interaction_api.wish.dto.WishStatus;
import ru.javabot.wish.dao.WishMapper;
import ru.javabot.wish.repository.ReferenceRepository;
import ru.javabot.wish.repository.WishRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishServiceImpl implements WishService{
    private final WishRepository wishRepository;
    private final ReferenceRepository referenceRepository;
    private final WishMapper wishMapper;

    @Override
    @Transactional
    public List<WishDto> createWishes(Long wishlistId, List<CreateWishRequest> wishRequestList) {
        List<String> uniqueReferences = wishRequestList.stream()
                .map(CreateWishRequest::getReference)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        List<ReferenceDao> existing = referenceRepository.findByReferenceIn(uniqueReferences);
        Map<String, ReferenceDao> existingMap = existing.stream()
                .collect(Collectors.toMap(ReferenceDao::getReference, r -> r));

        List<String> toCreate = uniqueReferences.stream()
                .filter(ref -> !existingMap.containsKey(ref))
                .toList();

        List<ReferenceDao> created = toCreate.isEmpty()
                ? List.of()
                : referenceRepository.saveAll(
                toCreate.stream()
                        .map(ref -> ReferenceDao.builder()
                                .reference(ref)
                                .build())
                        .toList()
        );

        Map<String, ReferenceDao> referenceMap = new HashMap<>(existingMap);
        referenceMap.putAll(created.stream()
                .collect(Collectors.toMap(ReferenceDao::getReference, r -> r)));

        List<WishDao> wishes = wishRequestList.stream()
                .map(req -> {
                    WishDao wish = wishMapper.toDao(req);
                    wish.setWishlistId(wishlistId);
                    wish.setStatus(WishStatus.AVAILABLE);

                    if (req.getReference() != null) {
                        wish.setReference(referenceMap.get(req.getReference()));
                    }

                    return wish;
                })
                .toList();

        return wishRepository.saveAll(wishes)
                .stream()
                .map(wishMapper::toDto)
                .toList();
    }
}
