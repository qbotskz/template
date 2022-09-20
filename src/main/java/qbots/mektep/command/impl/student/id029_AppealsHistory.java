package qbots.mektep.command.impl.student;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.FileType;
import qbots.mektep.enums.Role;
import qbots.mektep.enums.WaitingType;
import qbots.mektep.model.standart.AppealToPsych;
import qbots.mektep.model.standart.Student;
import qbots.mektep.model.standart.User;
import qbots.mektep.util.ButtonsLeaf;
import qbots.mektep.util.DateUtil;

import java.util.ArrayList;
import java.util.List;


public class id029_AppealsHistory extends Command {

    int delMess;
    int delMess2;

    ButtonsLeaf buttonsLeaf;
    List<AppealToPsych> appealToPsyches;
    AppealToPsych currentAppeal;

    User user;
    Student currentStudent;
    @Override
    public boolean execute() throws TelegramApiException {

        switch (waitingType){
            case START:
                deleteUpdateMess();
                user = usersRepo.findByChatId(chatId);
                currentStudent = user.getCurrentStudent();
                appealToPsyches = appealFromStudentRepo.findAllByStudent(currentStudent);
                sendAppeals();
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
                 if (hasCallbackQuery() && isButton(6)){ // back
                    sendAppeals();
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


        str += getText(610) + " " + currentAppeal.getText() + next +
        getText(615) + " " + getAnswer();

        deleteMessage(delMess);
        deleteMessage(delMess2);
        delMess2 = sendPhotoOrDoc();
        delMess = sendMessageWithKeyboard(str, 10);

        waitingType = WaitingType.CHOOSE_VARIANT;
    }

    private String getAnswer() {
        if (currentAppeal.getAnswer() != null)
            return currentAppeal.getAnswer();

        return getText(616);
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
            names.add(DateUtil.getDayDate(test.getSentDate()));
        }
        return names;
    }

    private void wrongData() throws TelegramApiException {
        sendMessage(4);
    }
}
