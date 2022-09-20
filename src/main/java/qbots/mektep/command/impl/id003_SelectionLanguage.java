package qbots.mektep.command.impl;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.Language;
import qbots.mektep.service.LanguageService;
import qbots.mektep.util.Const;


public class id003_SelectionLanguage extends Command {

    @Override
    public boolean execute() throws TelegramApiException {
        deleteMessage(updateMessageId);
        chosenLanguage();
        toDeleteMessage(sendMessage(Const.WELCOME_TEXT_WHEN_START));


        return EXIT;
    }

    private void chosenLanguage() {
        if (isButton(Const.RU_LANGUAGE)) LanguageService.setLanguage(chatId, Language.ru);
        if (isButton(Const.KZ_LANGUAGE)) LanguageService.setLanguage(chatId, Language.kz);
    }
}
