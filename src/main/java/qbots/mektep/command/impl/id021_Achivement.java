package qbots.mektep.command.impl;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.Role;
import qbots.mektep.enums.WaitingType;
import qbots.mektep.model.standart.Student;
import qbots.mektep.model.standart.User;
import qbots.mektep.util.ButtonsLeaf;
import qbots.mektep.util.DateKeyboard;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class id021_Achivement extends Command {
    User user;
    Student currentStudent;
    private int delMess;
//    DateKeyboard dateKeyboard;


    @Override
    public boolean execute() throws TelegramApiException {
        if (!isRegistered()){
            sendChooseLanguage();
            return EXIT;
        }

        switch (waitingType){
            case START:
                deleteUpdateMess();
                user = usersRepo.findByChatId(chatId);
                if (checkParent()){
                    sendStudents();
                }
                else {
                    sendMessage(165);
                    return EXIT;
                }
                return COMEBACK;
            case CHOOSE_STUDENT:
                deleteUpdateMess();
                if (hasCallbackQuery()){
                    currentStudent = studentRepo.findById(getLong(updateMessageText));
                    if (currentStudent != null){
                        sendText();
                    }
                    else {
                        wrongData();
                        sendStudents();
                    }
                }
                else {
                    wrongData();
                    sendStudents();
                }
                return COMEBACK;
            case SET_TEXT:
                if (hasMessageText()){
                        currentStudent.setAchievementText(updateMessageText);
                        sendPhoto();
                    }
                    else {
                        wrongData();
                    }
                return COMEBACK;
            case SET_PHOTO:
                deleteUpdateMess();
                if (hasPhoto()){
                    sendSuccess();
                    currentStudent.setAchievementFileid(updateMessagePhoto);
                    studentRepo.save(currentStudent);
                    return EXIT;
                }
                else{
                    wrongData();
                    sendPhoto();
                }
                return COMEBACK;

        }

        return COMEBACK;
    }

    private void sendPhoto() throws TelegramApiException {
        deleteMessage(delMess);
        delMess = sendMessage(162);
        waitingType = WaitingType.SET_PHOTO;
    }

    private void sendText() throws TelegramApiException {
        delete(delMess);
        delMess = sendMessage(161);
        waitingType = WaitingType.SET_TEXT;
    }



    private void sendSuccess() throws TelegramApiException {
        deleteMessage(delMess);
        sendMessage(163);
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

    private void sendStudents() throws TelegramApiException {
        deleteMessage(delMess);
        ButtonsLeaf buttonsLeaf = new ButtonsLeaf(getNames(studentRepo.findByParent(user)), getIds(studentRepo.findByParent(user)));
        delMess = sendMessageWithKeyboard(129, buttonsLeaf.getListButtonWithDataList());
        waitingType = WaitingType.CHOOSE_STUDENT;
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
