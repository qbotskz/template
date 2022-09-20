package qbots.mektep.command.impl.psychologist;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.FileType;
import qbots.mektep.enums.Role;
import qbots.mektep.enums.WaitingType;
import qbots.mektep.model.standart.AppealToPsych;
import qbots.mektep.model.standart.Classroom;
import qbots.mektep.model.standart.Student;
import qbots.mektep.model.standart.StudentsTest.Test;
import qbots.mektep.util.ButtonsLeaf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class id603_Appeals extends Command {

    int delMess;
    int delMess2;

    ButtonsLeaf buttonsLeaf;
    List<AppealToPsych> appealToPsyches;
    AppealToPsych currentAppeal;
    @Override
    public boolean execute() throws TelegramApiException {

        switch (waitingType){
            case START:
                deleteUpdateMess();
                if (isButton(601)) {
                    sendFromWho();
                }
                else if (isButton(606)){ // from students
                    appealToPsyches = appealFromStudentRepo.findAllByRoleAndAnswerIsNull(Role.ROLE_STUDENT);
                    sendAppeals();
                }
                else if (isButton(607)){ // from teachers
                    appealToPsyches = appealFromStudentRepo.findAllByRoleAndAnswerIsNull(Role.ROLE_CLASSROOM_TEACHER);
                    sendAppeals();
                }
                return COMEBACK;
            case SET_APPEAL:
                deleteUpdateMess();
                currentAppeal = appealFromStudentRepo.findById(getLong(updateMessageText));
                if (hasCallbackQuery() && currentAppeal!= null){
                    sendAppeal();
                }
                else {
                    wrongData();
                    sendAppeals();
                }
                return COMEBACK;
            case CHOOSE_VARIANT:
                deleteUpdateMess();
                if (hasCallbackQuery() && isButton(608)){ //to answer
                    sendText();
                }
                else if (hasCallbackQuery() && isButton(6)){ // back
                    sendAppeals();
                }
                return COMEBACK;
            case SET_TEXT:
                deleteUpdateMess();
                if (hasCallbackQuery() && isButton(6)){
                    sendAppeal();
                }
                else if (hasMessageText()){
                    currentAppeal.setAnswer(updateMessageText);
                    appealFromStudentRepo.save(currentAppeal);
                    sendSuccess();
                    appealToPsyches = appealFromStudentRepo.findAllByRoleAndAnswerIsNull(Role.ROLE_STUDENT);
                    sendAppeals();
                    sendAnswer();
                }
                else {
                    wrongData();
                    sendText();
                }
                return COMEBACK;

        }
        return EXIT;
    }

    private void sendAnswer() {
        String str = getText(613) + next+
                getText(614) + " " + currentAppeal.getAnswer();
        try {
            sendMessage(str, currentAppeal.getSenderChatId());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendText() throws TelegramApiException {
        deleteMessage(delMess);
        deleteMessage(delMess2);
        delMess = sendMessageWithKeyboard(611, 10);
        waitingType = WaitingType.SET_TEXT;
    }

    private void sendAppeals() throws TelegramApiException {
        deleteMessage(delMess);
        deleteMessage(delMess2);
        delMess = sendMessageWithKeyboard(606, new ButtonsLeaf(getNames(appealToPsyches), getIds(appealToPsyches)).getListButtonWithDataList());
        waitingType = WaitingType.SET_APPEAL;
    }

    private void sendFromWho() throws TelegramApiException {
        delMess = sendMessageWithKeyboard(605,20);
    }



    private void sendSuccess() throws TelegramApiException {
        deleteMessage(delMess);
        deleteMessage(delMess2);
        sendMessage(612);
    }

    private void sendAppeal() throws TelegramApiException {
        String str = "";
        if (currentAppeal.getRole().equals(Role.ROLE_STUDENT)){
            str = getText(607) + " " + currentAppeal.getStudent().getFullName() + next +
                    getText(608)+ " " + currentAppeal.getStudent().getClassroom().getName() + " " + getText(609)+ next;
        }
        else if (currentAppeal.getRole().equals(Role.ROLE_CLASSROOM_TEACHER)){
            str = getText(607) + " " + currentAppeal.getTeacher().getUser().getFullName()+ next;
        }

        str += getText(610) + " " + currentAppeal.getText();

        deleteMessage(delMess);
        deleteMessage(delMess2);
        delMess2 = sendPhotoOrDoc();
        delMess = sendMessageWithKeyboard(str, 21);

        waitingType = WaitingType.CHOOSE_VARIANT;
    }

    private int sendPhotoOrDoc() throws TelegramApiException {
        if (currentAppeal.getFileType().equals(FileType.photo)){
            return sendPhoto(currentAppeal.getFileId(),chatId);
        }
        else if (currentAppeal.getFileType().equals(FileType.document)){
            return sendDocument(currentAppeal.getFileId(),  chatId);
        }
        else if (currentAppeal.getFileType().equals(FileType.video)){
            return sendVideo(currentAppeal.getFileId(), chatId);
        }
        return 0;
    }


    private List<String> getIds(List<AppealToPsych> tests) {
        List<String> ids = new ArrayList<>();
        for (AppealToPsych test: tests){
            ids.add(String.valueOf(test.getId()));
        }
        return ids;
    }

    private List<String> getNames(List<AppealToPsych> appealToPsyches) {
        List<String> names = new ArrayList<>();
        for (AppealToPsych test: appealToPsyches){
            names.add(test.getSenderName());
        }
        return names;
    }

    private void wrongData() throws TelegramApiException {
        sendMessage(4);
    }
}
