package ru.javabot;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;


import java.util.List;

@Slf4j
@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;

    public UpdateConsumer(@Value("${telegram.bot.token}") String botToken) {
        this.telegramClient = new OkHttpTelegramClient(
                botToken
        );
    }

//    public UpdateConsumer() {
//        this.telegramClient = new OkHttpTelegramClient(
//                "8700103330:AAEUfG404m-8yizqEvsv5eMlsZ3R3f3YRIU"
//        );
//    }



    @SneakyThrows
    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            if (messageText.equals("/start")) {
                sandMainMenu(chatId);
            } else {
                System.out.printf(
                        "Пришло сообщение: %s от %s",
                        messageText,
                        chatId
                );

                SendMessage message = SendMessage.builder()
                        .text("Я не понимаю тебя((")
                        .chatId(chatId)
                        .build();

                telegramClient.execute(message);
            }
        }


    }

    private void sandMainMenu(Long chatId) throws TelegramApiException {
        SendMessage message = SendMessage.builder()
                .text("Выберете действие:")
                .chatId(chatId)
                .build();
        var button1 = InlineKeyboardButton.builder()
                .text("Создать новый список")
                .callbackData("my_name")
                .build();

        var button2 = InlineKeyboardButton.builder()
                .text("Добавить желание")
                .callbackData("your_name")
                .build();

        var button3 = InlineKeyboardButton.builder()
                .text("Удалить список")
                .callbackData("their_name")
                .build();
        List<InlineKeyboardRow> keyboardRows = List.of(
                new InlineKeyboardRow(button1),
                new InlineKeyboardRow(button2),
                new InlineKeyboardRow(button3)

        );
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(keyboardRows);

        message.setReplyMarkup(markup);
        telegramClient.execute(message);
    }
}
