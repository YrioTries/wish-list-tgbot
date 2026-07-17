package ru.javabot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {
    @Override
    public void consume(Update update) {
        log.info(
                "Пришло сообщение {} от {}",
                update.getMessage().getText(),
                update.getMessage().getChatId()
        );

        System.out.printf(
                "Пришло сообщение %s от %s",
                update.getMessage().getText(),
                update.getMessage().getChatId()
        );
    }
}
