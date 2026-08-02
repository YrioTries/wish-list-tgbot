package ru.javabot.wish.dao;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.javabot.interaction_api.user.dto.UserDto;

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

    @Column(name = "wish_name")
    String name;

    @Column(name = "owner_id", nullable = false)
    Long ownerId;

    @Column(name = "wish_description")
    String description;
}
