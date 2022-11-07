//package qbots.mektep.command.impl;
//
//import lombok.extern.slf4j.Slf4j;
//import org.telegram.telegrambots.meta.api.objects.InputFile;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import qbots.mektep.command.Command;
//import qbots.mektep.enums.FileType;
//import qbots.mektep.enums.Role;
//import qbots.mektep.enums.WaitingType;
//import qbots.mektep.model.standart.User;
//import qbots.mektep.service.MailingThread;
//import qbots.mektep.util.ButtonsLeaf;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//@Slf4j
//public class id043_MailingHeadTeacher extends Command {
//    User user;
//    private int delMess;
//    MailingThread mailingThread;
//
//    List<User> allUsers;
//
//    @Override
//    public boolean execute() throws TelegramApiException {
//        if (!isRegistered()){
//            sendChooseLanguage();
//            return EXIT;
//        }
//
//        switch (waitingType){
//            case START:
//                user = usersRepo.findByChatId(chatId);
//                if (!user.hasRole(Role.ROLE_HEAD_TEACHER_EDU)){
//                    sendMessage("У вас нетy доступа");
//                    return EXIT;
//                }
//                deleteUpdateMess();
//                mailingThread = new MailingThread(bot);
//                sendWho();
//                return COMEBACK;
//            case SET_WHO:
//                deleteUpdateMess();
//                if (hasCallbackQuery() && isButton(814)){ // all
//                    allUsers = usersRepo.findAll();
//                    mailingThread.setUsers(allUsers);
//                    sendMailingText();
//                }
//                else if (hasCallbackQuery() && isButton(815)){ // classroom teacher
//                    allUsers = usersRepo.findAllByRolesContains(Role.ROLE_CLASSROOM_TEACHER);
//                    mailingThread.setUsers(allUsers);
//                    sendMailingText();
//                }
//                else if (hasCallbackQuery() && isButton(816)){ // teacher
//                    allUsers = usersRepo.findAllByRolesContains(Role.ROLE_TEACHER);
//                    mailingThread.setUsers(allUsers);
//                    sendMailingText();
//                }
//                else if (hasCallbackQuery() && isButton(817)){ // parents
//                    allUsers = usersRepo.findAllByRolesContains(Role.ROLE_PARENT);
//                    mailingThread.setUsers(allUsers);
//                    sendMailingText();
//
//                }
//                else if (hasCallbackQuery() && isButton(818)){ // students
//                    allUsers = usersRepo.findAllByRolesContains(Role.ROLE_STUDENT);
//                    mailingThread.setUsers(allUsers);
//                    sendMailingText();
//                }
//                else {
//                    wrongData();
//                    sendWho();
//                }
//
//                return COMEBACK;
//            case SET_TEXT:
//                if (hasMessageText()){
//                    mailingThread.setText(updateMessageText);
//                    sendPVF();
//                }
//                else if (isButton(6)){
//                    sendClasses();
//                }
//                else {
//                    wrongData();
//                    sendMailingText();
//                }
//                return COMEBACK;
//            case SET_PHOTO_VIDEO_FILE:
//                deleteUpdateMess();
//                InputFile inputFile = new InputFile();
//                if (hasDocument()){
//                    inputFile.setMedia(update.getMessage().getDocument().getFileId());
//                    mailingThread.setInputFile(inputFile);
//                    mailingThread.setFileType(FileType.document);
//                    sendSuccess();
//                    mailingThread.start();
//                }
//                else if (hasPhoto()){
//                    inputFile.setMedia(update.getMessage().getPhoto().get(0).getFileId());
//                    mailingThread.setInputFile(inputFile);
//                    mailingThread.setFileType(FileType.photo);
//                    sendSuccess();
//                    mailingThread.start();
//                }
//                else if (hasVideo()){
//                    inputFile.setMedia(update.getMessage().getVideo().getFileId());
//                    mailingThread.setInputFile(inputFile);
//                    mailingThread.setFileType(FileType.video);
//                    sendSuccess();
//                    mailingThread.start();
//                }
//                else if (isButton(7)){
//                    mailingThread.setFileType(FileType.none);
//                    sendSuccess();
//                    mailingThread.start();
//                }
//                else {
//                    wrongData();
//                    sendPVF();
//                    return COMEBACK;
//
//                }
//                return EXIT;
//        }
//
//        return COMEBACK;
//    }
//
//
//
//
//
//    private void sendSuccess() throws TelegramApiException {
//        deleteMessage(delMess);
//        sendMessage(124);
//    }
//
//
//
//    private void sendPVF() throws TelegramApiException {
//        deleteMessage(delMess);
//        delMess = sendMessageWithKeyboard(123, 11);
//        waitingType = WaitingType.SET_PHOTO_VIDEO_FILE;
//    }
//
//    private void sendMailingText() throws TelegramApiException {
//        deleteMessage(delMess);
//        delMess = sendMessageWithKeyboard(122, 10);
//        waitingType = WaitingType.SET_TEXT;
//    }
//
//
//    private void sendWho() throws TelegramApiException {
//        deleteMessage(delMess);
//        delMess = sendMessageWithKeyboard(120, 28);
//        waitingType = WaitingType.SET_WHO;
//    }
//
//    protected boolean isLong(String mess) {
//        try {
//            Long.parseLong(mess);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//    protected long getLong(String mess) {
//        try {
//            return Long.parseLong(mess);
//        } catch (Exception e) {
//            return -1;
//        }
//    }
//
//    private void wrongData() throws TelegramApiException {
//        sendMessage(4);
//    }
//}
