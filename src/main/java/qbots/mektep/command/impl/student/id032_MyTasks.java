package qbots.mektep.command.impl.student;

import com.itextpdf.text.DocumentException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.FileType;
import qbots.mektep.enums.Role;
import qbots.mektep.enums.WaitingType;
import qbots.mektep.exceptions.ButtonNotFoundException;
import qbots.mektep.exceptions.CommandNotFoundException;
import qbots.mektep.exceptions.KeyboardNotFoundException;
import qbots.mektep.exceptions.MessageNotFoundException;
import qbots.mektep.model.standart.AppealToPsych;
import qbots.mektep.model.standart.IndividualTask;
import qbots.mektep.model.standart.Student;
import qbots.mektep.model.standart.StudentsTest.Question;
import qbots.mektep.model.standart.StudentsTest.StudentsAnswer;
import qbots.mektep.model.standart.StudentsTest.Test;
import qbots.mektep.model.standart.StudentsTest.Variant;
import qbots.mektep.model.standart.User;
import qbots.mektep.util.ButtonsLeaf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class id032_MyTasks extends Command {

    User user;
    private int delMess;
    private int delMess2;
    Student currentStudent;
    List<IndividualTask> tasks;
    IndividualTask currentTask;


    @Override
    public boolean execute() throws TelegramApiException, IOException, SQLException, FileNotFoundException, MessageNotFoundException, KeyboardNotFoundException, ButtonNotFoundException, CommandNotFoundException, DocumentException {
        if (!isRegistered()){
            sendChooseLanguage();
            return EXIT;
        }

        switch (waitingType){
            case START:

                deleteUpdateMess();
                user = usersRepo.findByChatId(chatId);
                currentStudent = user.getCurrentStudent();
                if (currentStudent != null){
                    if (isButton(407)){
                        sendMessageWithKeyboard(417, 22);
                    }
                    else if (isButton(408)){ // active
                        sendActiveTasks();
                    }
                    else if (isButton(409)){ // archive
//                        sendArchiveTasks();
                        //todo

                    }
                }
                else {
                    wrongData();
                }
                return COMEBACK;

            case CHOOSE_TASK:
                deleteUpdateMess();
                currentTask = individualTaskRepo.findById(getLong(updateMessageText));
                if (hasCallbackQuery() && currentTask!= null){
                     sendTask();
                }
                else {
                    wrongData();
                    sendActiveTasks();
                }
                return COMEBACK;
            case CHOOSE_VARIANT:
                deleteUpdateMess();
                if (hasCallbackQuery() && isButton(608)){ //to answer
                    sendText();
                }
                else if (hasCallbackQuery() && isButton(6)){ // back
                    sendActiveTasks();
                }
                return COMEBACK;
            case SET_TEXT:
                deleteUpdateMess();
                if (hasCallbackQuery() && isButton(6)){
                    sendTask();

                }
                else if (hasMessageText()){
                    currentTask.setAnswerText(updateMessageText);
                    sendFile();
                }
                else {
                    wrongData();
                    sendText();
                }
                return COMEBACK;
            case SET_PHOTO_VIDEO_FILE:
                deleteUpdateMess();
                if (hasPhoto()){
                    currentTask.setAnswerFileId(updateMessagePhoto);
                    currentTask.setAnswerFileType(FileType.photo);
                    individualTaskRepo.save(currentTask);
                    sendSuccess();
                    return EXIT;
                }
                else if (hasVideo()){
                    currentTask.setAnswerFileId(update.getMessage().getVideo().getFileId());
                    currentTask.setAnswerFileType(FileType.video);
                    individualTaskRepo.save(currentTask);
                    sendSuccess();
                    return EXIT;
                }
                else if (hasDocument()){
                    currentTask.setAnswerFileId(update.getMessage().getDocument().getFileId());
                    currentTask.setAnswerFileType(FileType.document);
                    individualTaskRepo.save(currentTask);
                    sendSuccess();
                    return EXIT;
                }
                else {
                    wrongData();
                    sendFile();
                }
                return COMEBACK;
            
        }


        return EXIT;
    }

    private void sendTask() throws TelegramApiException {
        String str = getText(419) + " " + currentTask.getTeacher().getUser().getFullName() + next+
                getText(420) + " " + currentTask.getText();

        delMess2 = sendPhotoOrDoc();
        delMess = sendMessageWithKeyboard(str,  21);
        waitingType = WaitingType.CHOOSE_VARIANT;

    }


    private int sendPhotoOrDoc() throws TelegramApiException {
        if (currentTask.getFileType().equals(FileType.photo)){
            return sendPhoto(currentTask.getFileId(),chatId);
        }
        else if (currentTask.getFileType().equals(FileType.document)){
            return sendDocument(currentTask.getFileId(),  chatId);
        }
        else if (currentTask.getFileType().equals(FileType.video)){
            return sendVideo(currentTask.getFileId(), chatId);
        }
        return 0;
    }

    private void sendActiveTasks() throws TelegramApiException {
        tasks = individualTaskRepo.findAllByStudentAndAnswerTextIsNull(currentStudent);
        deleteMessage(delMess);
        delMess = sendMessageWithKeyboard(418, new ButtonsLeaf(getNames2(tasks), getIds2(tasks)).getListButtonWithDataList());
        waitingType = WaitingType.CHOOSE_TASK;
    }



    private List<String> getIds2(List<IndividualTask> variants) {
        List<String> ids = new ArrayList<>();
        for (IndividualTask variant: variants){
            ids.add(String.valueOf(variant.getId()));
        }
        return ids;
    }

    private List<String> getNames2(List<IndividualTask> variants) {
        List<String> names = new ArrayList<>();
        for (IndividualTask variant: variants){
            names.add(variant.getText());
        }
        return names;
    }


    private List<String> getIds(List<Test> tests) {
        List<String> ids = new ArrayList<>();
        for (Test test: tests){
            ids.add(String.valueOf(test.getId()));
        }
        return ids;
    }

    private List<String> getNames(List<Test> tests) {
        List<String> names = new ArrayList<>();
        for (Test test: tests){
            names.add(test.getName());
        }
        return names;
    }

    private void sendFile() throws TelegramApiException {
        deleteMessage(delMess);
        delMess = sendMessage(403);
        waitingType = WaitingType.SET_PHOTO_VIDEO_FILE;
    }

    private void sendText() throws TelegramApiException {
        deleteMessage(delMess);
        delMess = sendMessage(421);
        waitingType = WaitingType.SET_TEXT;
    }

    private void sendSuccess() throws TelegramApiException {
        delete(delMess);
        sendMessage(422);
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
