package ru.javabot.wish.dao;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.javabot.interaction_api.wish.dto.WishStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "wishes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_id")
    Long id;

    @Size(min = 3, max = 150)
    @Column(name = "wish_name", nullable = false, length = 150)
    String name;

    @Column(name = "wishlist_id", nullable = false)
    Long wishlistId;

    @Column(name = "reference_id")
    Long referenceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    WishStatus status;

    @Column(name = "reserved_at")
    LocalDateTime reservedAt;

    @Column(name = "reserved_until")
    LocalDateTime reservedUntil;

    @PositiveOrZero
    @Column(name = "expected_price")
    Long expectedPrice;

    @Size(max = 1000)
    @Column(name = "wish_description", length = 1000)
    String description;
}
