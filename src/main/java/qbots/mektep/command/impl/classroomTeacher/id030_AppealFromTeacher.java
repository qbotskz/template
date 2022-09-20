package qbots.mektep.command.impl.classroomTeacher;

import com.itextpdf.text.DocumentException;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
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
import qbots.mektep.model.standart.Student;
import qbots.mektep.model.standart.Teacher;
import qbots.mektep.model.standart.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class id030_AppealFromTeacher extends Command {

    private AppealToPsych appealFromStudent;
    User user;
    private int delMess;
    Teacher teacher;



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
                teacher = teacherRepo.findByUserChatId(chatId);

                if (teacher != null){
                    sendText();
                    appealFromStudent = new AppealToPsych();
                    appealFromStudent.setTeacher(teacher);
                    appealFromStudent.setRole(Role.ROLE_CLASSROOM_TEACHER);
                    appealFromStudent.setSentDate(new Date());
                    return COMEBACK;
                }
                else {
                    wrongData();
                }
            case SET_TEXT:
                deleteUpdateMess();
                if (hasMessageText()){
                    appealFromStudent.setText(updateMessageText);
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
                    appealFromStudent.setFileId(updateMessagePhoto);
                    appealFromStudent.setFileType(FileType.photo);
                    appealFromStudentRepo.save(appealFromStudent);
                    sendSuccess();
                    return EXIT;
                }
                else if (hasVideo()){
                    appealFromStudent.setFileId(update.getMessage().getVideo().getFileId());
                    appealFromStudent.setFileType(FileType.video);
                    appealFromStudentRepo.save(appealFromStudent);
                    sendSuccess();
                    return EXIT;
                }
                else if (hasDocument()){
                    appealFromStudent.setFileId(update.getMessage().getDocument().getFileId());
                    appealFromStudent.setFileType(FileType.document);
                    appealFromStudentRepo.save(appealFromStudent);
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

    private void sendFile() throws TelegramApiException {
        deleteMessage(delMess);
        delMess = sendMessage(403);
        waitingType = WaitingType.SET_PHOTO_VIDEO_FILE;
    }

    private void sendText() throws TelegramApiException {
        deleteMessage(delMess);
        delMess = sendMessage(402);
        waitingType = WaitingType.SET_TEXT;
    }

    private void sendSuccess() throws TelegramApiException {
        delete(delMess);
        sendMessage(404);
        sendToPsychologists();
    }

    private void sendToPsychologists() throws TelegramApiException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getText(176)).append(" ").append(appealFromStudent.getSenderName()).append(next).
                append(getText(406)).append(" ").append(appealFromStudent.getText()).append(next).
                append(getText(407));



        List<User> users = usersRepo.findAllByRolesContains(Role.ROLE_PSYCHOLOGIST);
        for (User user : users){
            if (appealFromStudent.getFileType().equals(FileType.photo)){
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setPhoto(new InputFile(appealFromStudent.getFileId()));
                sendPhoto.setChatId(String.valueOf(user.getChatId()));
                sendPhoto.setCaption(stringBuilder.toString());
                bot.execute(sendPhoto);
            }
            else if (appealFromStudent.getFileType().equals(FileType.video)){
                SendVideo sendVideo = new SendVideo();
                sendVideo.setVideo(new InputFile(appealFromStudent.getFileId()));
                sendVideo.setChatId(String.valueOf(user.getChatId()));
                sendVideo.setCaption(stringBuilder.toString());
                bot.execute(sendVideo);
            }
            else if (appealFromStudent.getFileType().equals(FileType.document)){
                SendDocument sendDocument = new SendDocument();
                sendDocument.setDocument(new InputFile(appealFromStudent.getFileId()));
                sendDocument.setChatId(String.valueOf(user.getChatId()));
                sendDocument.setCaption(stringBuilder.toString());
                bot.execute(sendDocument);
            }
            else {
                sendMessage(stringBuilder.toString(), user.getChatId());
            }
        }
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
