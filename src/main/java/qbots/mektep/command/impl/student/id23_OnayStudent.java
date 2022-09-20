package qbots.mektep.command.impl.student;

import com.itextpdf.text.DocumentException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.WaitingType;
import qbots.mektep.exceptions.ButtonNotFoundException;
import qbots.mektep.exceptions.CommandNotFoundException;
import qbots.mektep.exceptions.KeyboardNotFoundException;
import qbots.mektep.exceptions.MessageNotFoundException;
import qbots.mektep.model.standart.Onay;
import qbots.mektep.model.standart.Student;
import qbots.mektep.model.standart.User;
import qbots.mektep.util.ButtonsLeaf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class id23_OnayStudent extends Command {

    private Onay onay = new Onay();
    User user;
    private int delMess;
    Student currentStudent;



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
                    onay.setStudent(currentStudent);
                    sendYesNo();
                    return COMEBACK;
                }
                else {
                    wrongData();
                }
            case ONAY:
                deleteUpdateMess();

                if (isButton(501)){
                    sendMessage(167);
                    return EXIT;

                } else if (isButton(502)) {
                    sendFIO();
                }
                return COMEBACK;
            case FIO:
                deleteUpdateMess();
                if(isButton(6)){
                    sendYesNo();
                    return COMEBACK;
                }
                if (hasMessageText()){
                    onay.setFullName(updateMessageText);
                    sendBDay();
                }
                return COMEBACK;
            case DATE_OF_BIRTH:
                deleteUpdateMess();
                if(isButton(6)){
                    sendFIO();
                    return COMEBACK;
                }
                if (hasMessageText()){
                    onay.setBirthday(updateMessageText);
                    sendLiter();
                }
                return COMEBACK;
            case LITER:
                deleteUpdateMess();
                if(isButton(6)){
                    sendBDay();
                    return COMEBACK;
                }
                if (hasMessageText()){
                    onay.setClassroom(updateMessageText);
                    sendCard();
                }
                return COMEBACK;
            case CARD:
                deleteUpdateMess();
                if(isButton(6)){
                    sendLiter();
                    return COMEBACK;
                }
                if (hasPhoto()){
                    onay.setCard(updateMessageText);
                    send3X4();

                }
                else {
                    wrongData();
                    sendCard();
                }
                return COMEBACK;
            case PHOTO:
                deleteUpdateMess();
                if(isButton(6)){
                    sendCard();
                    return COMEBACK;
                }
                if (hasPhoto()){
                    onay.setPhoto3x4(updateMessageText);
                    sendSuccess();
                    onayRepo.save(onay);
                    return EXIT;
                }
                else {
                    wrongData();
                    send3X4();
                }
                return COMEBACK;
        }


        return EXIT;
    }

    private void sendSuccess() throws TelegramApiException {
        delete(delMess);
        sendMessage(172);
    }

    private void send3X4() throws TelegramApiException {
        delete(delMess);
        delMess = sendMessageWithKeyboard(171, 10);
        waitingType = WaitingType.PHOTO;
    }

    private void sendCard() throws TelegramApiException {
        delete(delMess);
        delMess = sendMessageWithKeyboard(170, 10);
        waitingType = WaitingType.CARD;
    }

    private void sendLiter() throws TelegramApiException {
        delete(delMess);
        delMess = sendMessageWithKeyboard(169,10);
        waitingType = WaitingType.LITER;
    }

    private void sendBDay() throws TelegramApiException {
        delete(delMess);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        delMess = sendMessageWithKeyboard("Укажите дату\n" + "рождения ("+dtf.format(now)+")" , 10);
        waitingType = WaitingType.DATE_OF_BIRTH;
    }

    private void sendFIO() throws TelegramApiException {
        delete(delMess);
        delMess = sendMessageWithKeyboard(168,10);
        waitingType = WaitingType.FIO;
    }

    private void sendYesNo() throws TelegramApiException {
        delete(delMess);
        delMess = sendMessageWithKeyboard(166,500);
        waitingType = WaitingType.ONAY;
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
}
