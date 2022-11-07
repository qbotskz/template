package qbots.project.configuration;

import com.itextpdf.text.DocumentException;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.project.command.Command;
import qbots.project.enums.Language;
import qbots.project.exceptions.ButtonNotFoundException;
import qbots.project.exceptions.CommandNotFoundException;
import qbots.project.exceptions.KeyboardNotFoundException;
import qbots.project.exceptions.MessageNotFoundException;
import qbots.project.model.standart.Message;
import qbots.project.repository.MessageRepo;
import qbots.project.repository.TelegramBotRepositoryProvider;
import qbots.project.repository.UsersRepo;
import qbots.project.service.CommandService;
import qbots.project.service.KeyboardMarkUpService;
import qbots.project.service.LanguageService;
import qbots.project.util.DateUtil;
import qbots.project.util.SetDeleteMessages;
import qbots.project.util.UpdateUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

@Slf4j
public class Conversation {

    private Long chatId;

    private CommandService commandService = new CommandService();
    private MessageRepo messageRepo = TelegramBotRepositoryProvider.getMessageRepo();
    private KeyboardMarkUpService keyboardMarkUpService = new KeyboardMarkUpService();
    private UsersRepo usersRepo = TelegramBotRepositoryProvider.getUsersRepo();
    private static long currentChatId;
    private Command command;

    public void handleUpdate(Update update, DefaultAbsSender bot) throws TelegramApiException, SQLException, IOException, KeyboardNotFoundException, ButtonNotFoundException, MessageNotFoundException, CommandNotFoundException, DocumentException {
        printUpdate(update);
        chatId = UpdateUtil.getChatId(update);
        currentChatId = chatId;
        checkLanguage(chatId);
        try {
            command = commandService.getCommand(update);
            if (command != null) {
                SetDeleteMessages.deleteKeyboard(chatId, bot);
                SetDeleteMessages.deleteMessage(chatId, bot);
            }
        } catch (CommandNotFoundException e) {
            if (command == null) {
                sendLangKeyboard(bot);
            }
        }
        if (command != null) {
            if (command.isInitNormal(update, bot)) {
                clear();
                return;
            }
            boolean commandFinished = command.execute();
            if (commandFinished) clear();
        }
    }

    private void sendLangKeyboard(DefaultAbsSender bot) throws TelegramApiException {
        ReplyKeyboard replyKeyboard;
        Message message;
        if (usersRepo.findByChatId(chatId) == null) {
            replyKeyboard = keyboardMarkUpService.select(2);
            message = messageRepo.findByIdAndLanguageId(5, getLanguage().getId());
        } else {
            replyKeyboard = keyboardMarkUpService.select(1);
            message = messageRepo.findByIdAndLanguageId(1, getLanguage().getId());
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(message.getName());
        sendMessage.setReplyMarkup(replyKeyboard);
        bot.execute(sendMessage);
    }

    private void printUpdate(Update update) {
        String dataMessage = "";
        if (update.hasMessage())
            dataMessage = DateUtil.getDbMmYyyyHhMmSs(new Date((long) update.getMessage().getDate() * 1000));
        log.info("New update get {} -> send response {}", dataMessage, DateUtil.getDbMmYyyyHhMmSs(new Date()));
        log.info(UpdateUtil.toString(update));
    }

    private void checkLanguage(long chatId) {
        if (LanguageService.getLanguage(chatId) == null) LanguageService.setLanguage(chatId, Language.ru);
    }

    public static long getCurrentChatId() {
        return currentChatId;
    }

    private Language getLanguage() {
        if (chatId == 0) return Language.ru;
        return LanguageService.getLanguage(chatId);
    }

    private void clear() {
        command.clear();
        command = null;
    }
}
