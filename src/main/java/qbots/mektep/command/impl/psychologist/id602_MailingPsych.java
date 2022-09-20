package qbots.mektep.command.impl.psychologist;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.FileType;
import qbots.mektep.enums.Role;
import qbots.mektep.enums.WaitingType;
import qbots.mektep.model.standart.Classroom;
import qbots.mektep.model.standart.Student;
import qbots.mektep.model.standart.Teacher;
import qbots.mektep.model.standart.User;
import qbots.mektep.service.MailingThread;
import qbots.mektep.util.ButtonsLeaf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class id602_MailingPsych extends Command {
    User user;
    List<Classroom> classrooms;
    Classroom currentClassroom;
    private int delMess;
    MailingThread mailingThread;
    boolean isStudent;
    ButtonsLeaf buttonsLeaf2;
    @Override
    public boolean execute() throws TelegramApiException {
        if (!isRegistered()){
            sendChooseLanguage();
            return EXIT;
        }

        switch (waitingType){
            case START:
                user = usersRepo.findByChatId(chatId);
                if (!user.hasRole(Role.ROLE_PSYCHOLOGIST)){
                    sendMessage("У вас нетy доступа");
                    return EXIT;
                }
                deleteUpdateMess();
                mailingThread = new MailingThread(bot);
                sendWho();
                return COMEBACK;
            case SET_WHO:
                deleteUpdateMess();
                if (hasCallbackQuery() && (isButton(121) || isButton(122))){
                    if (isButton(121)){
                        isStudent = true;
                        sendClasses();
                    }
                    else if (isButton(122)){
                        isStudent = false;
                        classrooms = classroomRepo.findAllByTeacherUserChatId(chatId);
                        if (classrooms.size() == 1){
                            currentClassroom = classrooms.get(0);
                            mailingThread.setUsers(getParentsFromClassroom());
                            sendMailingText();
                        }
                        else if (classrooms.size() == 0){
                            deleteMessage(delMess);
                            sendMessage(125);
                            return EXIT;
                        }
                        else {
                            sendClasses();
                        }
                    }
                }
                else if (isButton(6)){
                    sendWho();
                }
                else {
                    wrongData();
                    sendWho();
                }
                return COMEBACK;
            case CHOOSE_CLASS:
                deleteUpdateMess();
                if (hasCallbackQuery()){
                    List<Classroom > classrooms = classroomRepo.findAllByNumber(Integer.parseInt(updateMessageText));

                    buttonsLeaf2 = new ButtonsLeaf(getNames3(classrooms),getIds3(classrooms) );
                    sendClasees2();
                }
                else {
                    wrongData();
                    sendClasses();
                }
                return COMEBACK;
            case SET_CLASS2:
                if (hasCallbackQuery()){
                    if(isButton(6)){
                        deleteUpdateMess();
                        sendClasses();
                    }
                    else if (classroomRepo.findById(getLong(updateMessageText))!= null){
                        currentClassroom = classroomRepo.findById(getLong(updateMessageText));
                        if (isStudent) {
                            mailingThread.setUsers(getUsersFromClassroom());
                        }
                        else {
                            mailingThread.setUsers(getParentsFromClassroom());
                        }
                        sendMailingText();
                    }
                    else {
                        wrongData();
                        sendClasees2();
                    }
                }
                else {
                    wrongData();
                    sendClasees2();
                }
                return COMEBACK;
            case SET_TEXT:
                if (hasMessageText()){
                    mailingThread.setText(updateMessageText);
                    sendPVF();
                }
                else if (isButton(6)){
                    sendClasses();
                }
                else {
                    wrongData();
                    sendMailingText();
                }
                return COMEBACK;
            case SET_PHOTO_VIDEO_FILE:
                deleteUpdateMess();
                InputFile inputFile = new InputFile();
                if (hasDocument()){
                    inputFile.setMedia(update.getMessage().getDocument().getFileId());
                    mailingThread.setInputFile(inputFile);
                    mailingThread.setFileType(FileType.document);
                    sendSuccess();
                    mailingThread.start();
                }
                else if (hasPhoto()){
                    inputFile.setMedia(update.getMessage().getPhoto().get(0).getFileId());
                    mailingThread.setInputFile(inputFile);
                    mailingThread.setFileType(FileType.photo);
                    sendSuccess();
                    mailingThread.start();
                }
                else if (hasVideo()){
                    inputFile.setMedia(update.getMessage().getVideo().getFileId());
                    mailingThread.setInputFile(inputFile);
                    mailingThread.setFileType(FileType.video);
                    sendSuccess();
                    mailingThread.start();
                }
                else if (isButton(7)){
                    mailingThread.setFileType(FileType.none);
                    sendSuccess();
                    mailingThread.start();
                }
                else {
                    wrongData();
                    sendPVF();
                    return COMEBACK;

                }
                return EXIT;
        }

        return COMEBACK;
    }

    private List<User> getParentsFromClassroom() {
        List<User> users = new ArrayList<>();

        for (Student student : currentClassroom.getStudents()){
            users.add(student.getParent());
        }
        return users;
    }

    private void sendClasees2() throws TelegramApiException {
        deleteMessage( delMess);

        delMess = sendMessageWithKeyboard("Выберите класс: " , buttonsLeaf2.getListButtonWithDataList());
        waitingType = WaitingType.SET_CLASS2;
    }

    private void sendSuccess() throws TelegramApiException {
        deleteMessage(delMess);
        sendMessage(124);
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

    private void sendClasses() throws TelegramApiException {
        deleteMessage( delMess);
        List<Classroom> classrooms = classroomRepo.findAll();
        Set<String> nums = new HashSet<>();

        for (Classroom classroom : classrooms){
            nums.add(String.valueOf(classroom.getNumber()));
        }
        List<String> mainList = new ArrayList<>(nums);
        ButtonsLeaf buttonsLeaf = new ButtonsLeaf(new ArrayList<String>(nums),new ArrayList<String>(nums) );
        delMess = sendMessageWithKeyboard("Выберите класс: " , buttonsLeaf.getListButtonWithDataList());
        waitingType = WaitingType.CHOOSE_CLASS;
    }
    private List<String> getNames3(List<Classroom> classrooms) {
        List<String> names = new ArrayList<>();
        for (Classroom classroom : classrooms){
            names.add(classroom.getName());
        }
        names.add(getButtonText(6));
        return names;
    }

    private List<String> getIds3(List<Classroom> classrooms) {
        List<String> names = new ArrayList<>();
        for (Classroom classroom : classrooms){
            names.add(String.valueOf(classroom.getId()));
        }
        names.add(getButtonText(6));
        return names;
    }

    private List<String> getIds(List<Classroom> classrooms) {
        List<String> names = new ArrayList<>();
        for (Classroom classroom : classrooms){
            names.add(String.valueOf(classroom.getId()));
        }
        names.add(getButtonText(6));
        return names;
    }

    private List<String> getNames(List<Classroom> classrooms) {
        List<String> names = new ArrayList<>();
        for (Classroom classroom : classrooms){
            names.add(classroom.getName());
        }
        names.add(getButtonText(6));
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
