package ru.javabot.wish.service;

import ru.javabot.wish.dao.WishDao;
import ru.javabot.wish.dao.WishStatus;

import java.time.Clock;
import java.time.LocalDateTime;

public class WishServiceImpl {


    boolean isExpired(WishDao wish, Clock clock) {
        return wish.getStatus() == WishStatus.RESERVED
                && wish.getReservedAt() != null
                && wish.getReservedAt()
                .plusMonths(2)
                .isBefore(LocalDateTime.now(clock));
    }
}
