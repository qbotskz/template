package qbots.mektep.configuration;

import com.itextpdf.text.DocumentException;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.exceptions.ButtonNotFoundException;
import qbots.mektep.exceptions.CommandNotFoundException;
import qbots.mektep.exceptions.KeyboardNotFoundException;
import qbots.mektep.exceptions.MessageNotFoundException;
import qbots.mektep.repository.PropertiesRepo;
import qbots.mektep.repository.TelegramBotRepositoryProvider;
import qbots.mektep.util.UpdateUtil;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class
Bot extends TelegramLongPollingBot {

    private PropertiesRepo propertiesRepo = TelegramBotRepositoryProvider.getPropertiesRepo();
    private Map<Long, Conversation> conversations = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {
        Conversation conversation = getConversation(update);
        try {
            conversation.handleUpdate(update, this);
        } catch (TelegramApiException | SQLException | FileNotFoundException e) {
            log.error("Error " + e);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessageNotFoundException e) {
            e.printStackTrace();
        } catch (KeyboardNotFoundException e) {
            e.printStackTrace();
        } catch (ButtonNotFoundException e) {
            e.printStackTrace();
        } catch (CommandNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private Conversation getConversation(Update update) {
        Long chatId = UpdateUtil.getChatId(update);
        Conversation conversation = conversations.get(chatId);
        if (conversation == null) {
            log.info("InitNormal new conversation for '{}'", chatId);
            conversation = new Conversation();
            conversations.put(chatId, conversation);
        }
        return conversation;
    }

    @Override
    public String getBotUsername() {
        return "dev_kasym_bot";

//        return propertiesRepo.findById(Const.BOT_NAME).get().getValue();
    }

    @Override
    public String getBotToken() {
        return "1782218608:AAFaQO3GeTOA9zLoKPTYGMyKW_m4TeW2nCI";
//        return propertiesRepo.findById(Const.BOT_TOKEN).get().getValue();
    }
}
