package ru.javabot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;

@Component
public class BotController implements SpringLongPollingBot {

    @Override
    public String getBotToken() {
        return "8700103330:AAEUfG404m-8yizqEvsv5eMlsZ3R3f3YRIU";
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return null;
    }
}
