package ru.javabot.telegrambot;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
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

    @SneakyThrows
    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start" -> sendMainMenu(chatId);
                case "/keyboard" -> sendKeyBoard(chatId);
                case "Привет" -> sendMyName(chatId, update.getMessage().getFrom());
                default -> {
                    System.out.printf(
                            "Пришло сообщение: %s от %s",
                            messageText,
                            chatId
                    );

                    sendMessage(chatId, "Я пока не знаю как отвечать на такие сообщения, но я быстро учусь!"
                    );
                }
            }
        } else if (update.hasCallbackQuery()) {
            handleCallBackQuery(update.getCallbackQuery());

        }
    }

    private void sendMyName(Long chatId, User user) {
        var text = "Привет!\n\nТебя зовут: %s\nВаш ник: @%s"
                .formatted(
                        user.getFirstName() + " " + user.getLastName(),
                        user.getUserName()
                );

        sendMessage(chatId, text);
    }

    private void sendKeyBoard(Long chatId) throws TelegramApiException {

        SendMessage message = SendMessage.builder()
                .text("Обычная клавиатура")
                .chatId(chatId)
                .build();

        List<KeyboardRow> keyboardRows = List.of(
                new KeyboardRow("Привет", "Пока", "Ариведерчи")
        );

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(keyboardRows);
        message.setReplyMarkup(markup);
        telegramClient.execute(message);
    }


    private void handleCallBackQuery(CallbackQuery callbackQuery) {
        var data = callbackQuery.getData();
        var chatId = callbackQuery.getFrom().getId();
        var user = callbackQuery.getFrom();

        switch (data) {
            case "first_button" -> sendMessage(chatId, "Создаю новый список");
            case "second_button" -> sendMessage(chatId, "Добавляю желание");
            case "third_button" -> sendMessage(chatId, "Удаляю список");
            default -> sendMessage(chatId, "Нажмите на кнопку");
        }
    }

    @SneakyThrows
    private void sendMessage(Long chatId, String messageText) {
        SendMessage message = SendMessage.builder()
                .text(messageText)
                .chatId(chatId)
                .build();

        telegramClient.execute(message);
    }

    private void sendMainMenu(Long chatId) throws TelegramApiException {
        SendMessage message = SendMessage.builder()
                .text("Выберете действие:")
                .chatId(chatId)
                .build();
        var button1 = InlineKeyboardButton.builder()
                .text("Создать новый список")
                .callbackData("first_button")
                .build();

        var button2 = InlineKeyboardButton.builder()
                .text("Добавить желание")
                .callbackData("second_button")
                .build();

        var button3 = InlineKeyboardButton.builder()
                .text("Удалить список")
                .callbackData("third_button")
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
