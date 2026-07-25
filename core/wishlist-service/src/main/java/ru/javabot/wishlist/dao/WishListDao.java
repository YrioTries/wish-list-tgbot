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
    Long id;

    @Column(name = "wish_id", nullable = false)
    Long wishId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reference_id", nullable = false)
    ReferenceDao referenceDao;

    @Column(name = "owner_nickname", nullable = false, length = 50)
    String ownerNickname;

    @Column(name = "reserved_at", nullable = false)
    LocalDateTime reservedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    WishStatus status;

    @Column(name = "comment", length = 500)
    String comment;
}
