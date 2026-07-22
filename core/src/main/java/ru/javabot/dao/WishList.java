package ru.javabot.dao;

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
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wish_id", nullable = false)
    Wish wish;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reference_id", nullable = false)
    Reference reference;

    @Column(name = "giver_nickname", nullable = false, length = 50)
    String giverNickname;

    @Column(name = "reserved_at", nullable = false)
    LocalDateTime reservedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    WishStatus status;

    @Column(name = "comment", length = 500)
    String comment;
}
