package qbots.project.command.impl;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.project.command.Command;

public class id001_ShowInfo extends Command {

    @Override
    public boolean execute() throws TelegramApiException {
        deleteMessage(updateMessageId);

        if (isButton(18)){
            sendChooseLanguage();
        }
//
//        sendMessageWithAddition();

        return EXIT;
    }
}
