package ru.javabot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Component
@RequiredArgsConstructor
public class BotController implements SpringLongPollingBot {

//    @Value("${telegram.bot.token}")
//    private String botToken;

    private final UpdateConsumer updateConsumer;

    @Override
    public String getBotToken() {
        return "8700103330:AAEUfG404m-8yizqEvsv5eMlsZ3R3f3YRIU";
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return updateConsumer;
    }
}
