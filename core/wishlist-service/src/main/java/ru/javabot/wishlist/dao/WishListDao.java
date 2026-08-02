package ru.javabot.wishlist.dao;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "wish_list",
        uniqueConstraints = @UniqueConstraint(columnNames = {"wish_id", "reference_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishListDao {

    @Id
    @Column(name = "wishlist_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id; // ID вишлиста

    @Column(name = "wish_id", nullable = false)
    Long wishId; // ID желания

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_id")
    ReferenceDao referenceDao; // Сущность хранящая ссылки на электронный ресурс где можно купить подарок

    @Column(name = "owner_id", nullable = false)
    Long ownerId;

    @Column(name = "reserved_at")
    LocalDateTime reservedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    WishStatus status;

    @Column(name = "comment", length = 500)
    String comment;
}
