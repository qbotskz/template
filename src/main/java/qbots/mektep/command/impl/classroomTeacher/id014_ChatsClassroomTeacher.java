package qbots.mektep.command.impl.classroomTeacher;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.WaitingType;
import qbots.mektep.model.standart.*;
import qbots.mektep.util.ButtonsLeaf;
import qbots.mektep.util.DateKeyboard;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class id014_ChatsClassroomTeacher extends Command {
    User user;
//    Classroom currentClassroom;
//    Student currentStudent;
    private int delMess;
    DateKeyboard dateKeyboard;
    Teacher teacher;


    @Override
    public boolean execute() throws TelegramApiException {
        if (!isRegistered()){
            sendChooseLanguage();
            return EXIT;
        }

        switch (waitingType){
            case START:
                deleteUpdateMess();
                teacher = teacherRepo.findByUserChatId(chatId);
                if (teacher != null){
//                    currentClassroom = teacher.getCurrentClassroom();
                    sendChats();
                }
                else {
                    sendMessage("У вас нет доступа");
                    return EXIT;
                }
                return COMEBACK;


        }

        return COMEBACK;
    }

    private void sendChats() throws TelegramApiException {
        sendMessageWithKeyboard(153, getLinkButton());
    }


    private ReplyKeyboard getLinkButton() {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        String link1 = teacher.getCurrentClassroom().getChatLinkChildren();
        String name1 = teacher.getCurrentClassroom().getNumber() + teacher.getCurrentClassroom().getLetter()+"(ученики)";


        String link2 = teacher.getCurrentClassroom().getChatLinkParents();
        String name2 = teacher.getCurrentClassroom().getNumber() + teacher.getCurrentClassroom().getLetter()+"(родители)";


        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setUrl(link1);
        button.setText(name1);
        row.add(button);


        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setUrl(link2);
        button2.setText(name2);
        row.add(button2);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        String name3 = getButtonText(135);
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText(name3);
        button3.setCallbackData(name3);
        row2.add(button3);

        rows.add(row);
        rows.add(row2);

        keyboard.setKeyboard(rows);

        return keyboard;
    }

    private void sendPhoto() throws TelegramApiException {
        deleteMessage(delMess);
        delMess = sendMessage(131);
        waitingType = WaitingType.SET_PHOTO;
    }

    private void sendDate() throws TelegramApiException {
        delete(delMess);
        delMess = sendMessageWithKeyboard(130, dateKeyboard.getCalendarKeyboard());
        waitingType = WaitingType.SET_DATE;
    }




    private void sendSuccess() throws TelegramApiException {
        deleteMessage(delMess);
        sendMessage(124);
    }

    private void sendPVF() throws TelegramApiException {
        deleteMessage(delMess);
        delMess = sendMessageWithKeyboard(123, 11);
        waitingType = WaitingType.SET_PHOTO_VIDEO_FILE;
    }

    private void sendMailingText() throws TelegramApiException {
        deleteMessage(delMess);
        delMess = sendMessageWithKeyboard(122, 10);
        waitingType = WaitingType.SET_TEXT;
    }



    private List<String> getIds(List<Student> students) {
        List<String> names = new ArrayList<>();
        for (Student student : students){
            names.add(String.valueOf(student.getId()));
        }
        return names;
    }

    private List<String> getNames(List<Student> students) {
        List<String> names = new ArrayList<>();
        for (Student student : students){
            names.add(student.getFullName());
        }
        return names;
    }

    private void sendWho() throws TelegramApiException {
        deleteMessage(delMess);
        delMess = sendMessageWithKeyboard(120, 9);
        waitingType = WaitingType.SET_WHO;
    }

    protected boolean isLong(String mess) {
        try {
            Long.parseLong(mess);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    protected long getLong(String mess) {
        try {
            return Long.parseLong(mess);
        } catch (Exception e) {
            return -1;
        }
    }

    private void wrongData() throws TelegramApiException {
        sendMessage(4);
    }
}
