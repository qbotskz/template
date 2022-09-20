package qbots.mektep.command.impl.classroomTeacher;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.WaitingType;
import qbots.mektep.model.standart.*;
import qbots.mektep.util.ButtonsLeaf;
import qbots.mektep.util.DateKeyboard;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class id016_ReferencesStudents extends Command {
    User user;
    Classroom currentClassroom;
    Student currentStudent;
    private int delMess;
    DateKeyboard dateKeyboard;
    Teacher teacher;

    ReferencesStudents referencesStudents;


    @Override
    public boolean execute() throws TelegramApiException {
        if (!isRegistered()){
            sendChooseLanguage();
            return EXIT;
        }

        switch (waitingType){
            case START:
                deleteUpdateMess();
                referencesStudents = new ReferencesStudents();
                teacher = teacherRepo.findByUserChatId(chatId);
                if (teacher != null){
                    currentClassroom = teacher.getCurrentClassroom();
                }
                else {
                    sendMessage("У вас нет доступа");
                    return EXIT;
                }
                if(isButton(133)){
                    sendMessageWithKeyboard(154, 14);
                }
                else if (isButton(131)){
                    sendStudents();
                    referencesStudents.setType(updateMessageText);
                }
                else if (isButton(132)){
                    sendStudents();
                    referencesStudents.setType(updateMessageText);
                }

                return COMEBACK;
            case CHOOSE_STUDENT:
                dateKeyboard = new DateKeyboard();
                deleteUpdateMess();
                if (hasCallbackQuery()){
                    currentStudent = studentRepo.findById(getLong(updateMessageText));
                    if (currentStudent != null){
                        sendDate();
                        referencesStudents.setStudent(currentStudent);
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
            case SET_DATE:
                if (hasCallbackQuery()){
                    if (dateKeyboard.isNext(updateMessageText)){
                        editMessageWithKeyboard(getTypeMsg(referencesStudents.getType()), dateKeyboard.getCalendarKeyboard(), updateMessageId);
                    }
                    else if (dateKeyboard.getDateDate(updateMessageText) != null){
                        referencesStudents.setDateStart(dateKeyboard.getDateDate(updateMessageText));

                        sendDateEnd();
                    }
                    else {
                        wrongData();
                    }

                }
                return COMEBACK;
            case SET_DATE_END:
                if (hasCallbackQuery()){
                    if (dateKeyboard.isNext(updateMessageText)){
                        editMessageWithKeyboard(getTypeMsg2(referencesStudents.getType()), dateKeyboard.getCalendarKeyboard(), updateMessageId);
                    }
                    else if (dateKeyboard.getDateDate(updateMessageText) != null){
                        referencesStudents.setDateEnd(dateKeyboard.getDateDate(updateMessageText));

                        sendPhoto();
                    }
                    else {
                        wrongData();
                    }

                }
                return COMEBACK;
            case SET_PHOTO:
                deleteUpdateMess();
                if (hasPhoto()){
                    sendSuccess();
                    referencesStudents.setPhotoId(updateMessagePhoto);
                    referencesStudentsRepo.save(referencesStudents);
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

    private void sendDateEnd() throws TelegramApiException {
        delete(delMess);
        delMess = sendMessageWithKeyboard(getTypeMsg2(referencesStudents.getType()), dateKeyboard.getCalendarKeyboard());
        waitingType = WaitingType.SET_DATE_END;
    }

    private String getTypeMsg2(String type) {
        if (getButtonText(131).equals(referencesStudents.getType())){
            return getText(157);
        }
            return getText(158);
    }
    private String getTypeMsg(String type) {
        if (getButtonText(131).equals(referencesStudents.getType())){
            return getText(155);
        }
            return getText(156);
    }

    private void sendPhoto() throws TelegramApiException {
        deleteMessage(delMess);
        delMess = sendMessage(159);
        waitingType = WaitingType.SET_PHOTO;
    }

    private void sendDate() throws TelegramApiException {
        delete(delMess);
        delMess = sendMessageWithKeyboard(getTypeMsg(referencesStudents.getType()), dateKeyboard.getCalendarKeyboard());
        waitingType = WaitingType.SET_DATE;
    }


    private List<User> getParentsFromClassroom() {
        List<User> users = new ArrayList<>();

        for (Student student : currentClassroom.getStudents()){
            users.add(student.getParent());
        }
        return users;
    }

    private void sendSuccess() throws TelegramApiException {
        deleteMessage(delMess);
        sendMessage(160);
    }

    private List<User> getUsersFromClassroom() {
        List<User> users = new ArrayList<>();

        for (Student student : currentClassroom.getStudents()){
            users.add(student.getUser());
        }

        return users;
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
        ButtonsLeaf buttonsLeaf = new ButtonsLeaf(getNames(currentClassroom.getStudents()), getIds(currentClassroom.getStudents()));
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
