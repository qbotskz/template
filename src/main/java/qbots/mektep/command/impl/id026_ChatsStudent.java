package qbots.mektep.command.impl;

import com.itextpdf.text.DocumentException;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.WaitingType;
import qbots.mektep.exceptions.ButtonNotFoundException;
import qbots.mektep.exceptions.CommandNotFoundException;
import qbots.mektep.exceptions.KeyboardNotFoundException;
import qbots.mektep.exceptions.MessageNotFoundException;
import qbots.mektep.model.standart.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class id026_ChatsStudent extends Command {

    private AppealToPsych appealFromStudent;
    User user;
    private int delMess;
    Student currentStudent;
    Teacher teacher;
    Classroom currentClassroom;
    boolean isParent;

    @Override
    public boolean execute() throws TelegramApiException, IOException, SQLException, FileNotFoundException, MessageNotFoundException, KeyboardNotFoundException, ButtonNotFoundException, CommandNotFoundException, DocumentException {
        if (!isRegistered()) {
            sendChooseLanguage();
            return EXIT;
        }

        switch (waitingType) {
            case START:
                deleteUpdateMess();
                teacher = teacherRepo.findByUserChatId(chatId);
                if (teacher != null) {
                    currentClassroom = teacher.getCurrentClassroom();
                    sendWhichChat();
                } else {
                    sendMessage("У вас нет доступа");
                    return EXIT;
                }
                return COMEBACK;
            case SET_CHATS:
                deleteUpdateMess();
                if (hasCallbackQuery()) {
                    if (isButton(136)) {
                        isParent = true;
                        sendLink();
                    }
                    else if (isButton(137)) {
                        isParent = false;
                    }
                    else {
                        wrongData();
                        sendWhichChat();
                    }
                }
                else {
                    wrongData();
                    sendWhichChat();
                }
                return COMEBACK;
            case SET_TEXT:
                deleteUpdateMess();
                if (hasMessageText()){
                    if (isParent) {
                        currentClassroom.setChatLinkParents(updateMessageText);
                    }
                    else {
                        currentClassroom.setChatLinkChildren(updateMessageText);
                    }
                    classroomRepo.save(currentClassroom);
                    sendSuccess();
                    return EXIT;
                }
                return COMEBACK;
        }

        return EXIT;
    }

    private void sendSuccess() throws TelegramApiException {
        delete(delMess);
        sendMessage(175);

    }

    private void sendLink() throws TelegramApiException {
        delete(delMess);
        delMess = sendMessage(174);
        waitingType = WaitingType.SET_TEXT;
    }

    private void sendWhichChat() throws TelegramApiException {
        delete(delMess);
        delMess = sendMessageWithKeyboard(173, 16);
        waitingType = WaitingType.SET_CHATS;
    }

    private void sendChats() throws TelegramApiException {
        sendMessageWithKeyboard(153, getLinkButton());
    }


    private ReplyKeyboard getLinkButton() {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        String link1 = currentStudent.getClassroom().getChatLinkChildren();
        String name1 = currentStudent.getClassroom().getNumber() + currentStudent.getClassroom().getLetter() + "(ученики)";

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setUrl(link1);
        button.setText(name1);
        row.add(button);


        rows.add(row);

        keyboard.setKeyboard(rows);

        return keyboard;
    }


    private void wrongData() throws TelegramApiException {
        sendMessage(4);
    }

    protected long getLong(String mess) {
        try {
            return Long.parseLong(mess);
        } catch (Exception e) {
            return -1;
        }
    }


}
