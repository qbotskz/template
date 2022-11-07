package qbots.project.command.impl;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.project.command.Command;
import qbots.project.enums.Language;
import qbots.project.enums.WaitingType;
import qbots.project.model.standart.*;
import qbots.project.service.LanguageService;
import qbots.project.util.UpdateUtil;

import java.util.*;


public class id002_Registration extends Command {
    int delMes;

    User user;
    @Override
    public boolean execute() throws TelegramApiException {

        switch (waitingType) {
            case START:
                if (isButton(1)) {
                    delMes = sendLanguages();
                }
                else if (isButton(3)) { // kz
                    deleteMessage(delMes);
                    deleteMessage(updateMessageId);
                    LanguageService.setLanguage(chatId, Language.kz);

                    if (!isRegistered()) {
                        sendIIN();
                        return COMEBACK;
                    }
                    else {
                        sendRoleMenu();
                        return EXIT;
                    }


                }
                else if (isButton(4)) { // ru
                    deleteMessage(delMes);

                    deleteMessage(updateMessageId);
                    LanguageService.setLanguage(chatId, Language.ru);

                    if (!isRegistered()) {
                        sendIIN();
                        return COMEBACK;
                    }
                    else {
                        sendRoleMenu();
                        return EXIT;
                    }


                }

                return COMEBACK;
            case SET_IIN:
                deleteMessage(delMes);
                deleteMessage(updateMessageId);
                if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().length() <= 50) {
                    user.setIin(update.getMessage().getText());
                    sendPhone();
                    waitingType = WaitingType.SET_PHONE_NUMBER;
                } else {
                    wrongData();
                    sendIIN();
                }
                return COMEBACK;
            case SET_PHONE_NUMBER:
                if (hasContact()) {
                    deleteMessage(delMes);
                    deleteMessage(updateMessageId);
                    String phone = update.getMessage().getContact().getPhoneNumber();
                    if (update.getMessage().getContact().getPhoneNumber().startsWith("8")) {
                        phone = update.getMessage().getContact().getPhoneNumber().replaceFirst("8", "+7");
                    }
                    if (update.getMessage().getContact().getPhoneNumber().startsWith("7")) {
                        phone = update.getMessage().getContact().getPhoneNumber().replaceFirst("7", "+7");
                    }
                    user.setPhone(phone);
                    user.setUsername(UpdateUtil.getFrom(update));
                    user = usersRepo.save(user);


                    sendStartMenu();
                    return COMEBACK;
                } else {
                    wrongData();
                    sendPhone();
                    return COMEBACK;
                }

        }
        return EXIT;

    }

    private void sendRoleMenu() throws TelegramApiException {
        User user667 = usersRepo.findByChatId(chatId);
        if(user667.getChosenRole() == null)
            sendStartMenu();

        switch (user667.getChosenRole()){
            case ROLE_ADMIN:            sendAdminPanel();break;
            case ROLE_PATIENT: sendPatientPanel();break;
            case ROLE_NURSE :sendNursePanel();break;
            default:sendStartMenu();break;
        }
    }







    protected long getLong(String mess) {
        try {
            return Long.parseLong(mess);
        } catch (Exception e) {
            return -1;
        }
    }
    private int sendLanguages() throws TelegramApiException {
        return sendMessageWithKeyboard(5,2);
    }

    private void wrongData() throws TelegramApiException {
        sendMessage(4);
    }

    private void sendPhone() throws TelegramApiException {

        delMes = sendMessageWithKeyboard(3, getContactKeyboard());
    }

    private ReplyKeyboard getContactKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();

        Button buttonFromDb = buttonRepo.findByIdAndLangId(2, getLanguage().getId());
        KeyboardButton button = new KeyboardButton();
        String buttonText = buttonFromDb.getName();
        button.setText(buttonText);
        button.setRequestContact(true);
        keyboardRow.add(button);

        keyboardRowList.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    private int sendIIN() throws TelegramApiException {
        delMes=sendMessage(2);
        waitingType = WaitingType.SET_IIN;

        user = usersRepo.findByChatId(chatId);
        if (user == null){
            user = new User();
            user.setChatId(chatId);
        }

        return delMes;
    }
}
