package qbots.mektep.command.impl;

import com.itextpdf.text.DocumentException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.Role;
import qbots.mektep.enums.WaitingType;
import qbots.mektep.exceptions.ButtonNotFoundException;
import qbots.mektep.exceptions.CommandNotFoundException;
import qbots.mektep.exceptions.KeyboardNotFoundException;
import qbots.mektep.exceptions.MessageNotFoundException;
import qbots.mektep.model.standart.Onay;
import qbots.mektep.model.standart.RequestFromParent;
import qbots.mektep.model.standart.Student;
import qbots.mektep.model.standart.User;
import qbots.mektep.util.ButtonsLeaf;
import qbots.mektep.util.Const;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class id201_ReqFromParent extends Command {

    String oldUpdateMessageText;

    @Override
    public boolean execute() throws TelegramApiException, IOException, SQLException, FileNotFoundException, MessageNotFoundException, KeyboardNotFoundException, ButtonNotFoundException, CommandNotFoundException, DocumentException {

        if (!isRegistered()){
            sendChooseLanguage();
            return EXIT;
        }

        oldUpdateMessageText = updateMessageText;
        updateMessageText = updateMessageText.split(Const.SPLIT)[0];

        RequestFromParent requestFromParent = requestFromParentRepo.findById(getLong(oldUpdateMessageText.split(Const.SPLIT)[1]));


        Student student = requestFromParent.getStudent();
        User parent = requestFromParent.getParent();

        if (isButton(503)) { //accept

            student.setParent(parent);
            studentRepo.save(student);
            parent.addRole(Role.ROLE_PARENT);
            usersRepo.save(parent);

            sendMessageWithKeyboard(164, 1, parent.getChatId());

            editMessage(update.getCallbackQuery().getMessage().getText() +next+ "Принято!", chatId, updateMessageId);
        }
        else if (isButton(504)) {                // reject{
            sendMessage("Ваша заявка отклонена!", parent.getChatId());
            editMessage(update.getCallbackQuery().getMessage().getText() +next+ "Отклонено!", chatId, updateMessageId);
        }


        return EXIT;
    }



    private void wrongData() throws TelegramApiException {
        sendMessage(4);
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
