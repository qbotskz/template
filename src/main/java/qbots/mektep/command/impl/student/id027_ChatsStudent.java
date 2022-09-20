package qbots.mektep.command.impl.student;

import com.itextpdf.text.DocumentException;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.exceptions.ButtonNotFoundException;
import qbots.mektep.exceptions.CommandNotFoundException;
import qbots.mektep.exceptions.KeyboardNotFoundException;
import qbots.mektep.exceptions.MessageNotFoundException;
import qbots.mektep.model.standart.AppealToPsych;
import qbots.mektep.model.standart.Student;
import qbots.mektep.model.standart.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class id027_ChatsStudent extends Command {

    private AppealToPsych appealFromStudent;
    User user;
    private int delMess;
    Student currentStudent;



    @Override
    public boolean execute() throws TelegramApiException, IOException, SQLException, FileNotFoundException, MessageNotFoundException, KeyboardNotFoundException, ButtonNotFoundException, CommandNotFoundException, DocumentException {
        if (!isRegistered()){
            sendChooseLanguage();
            return EXIT;
        }

        deleteUpdateMess();
        user = usersRepo.findByChatId(chatId);
        currentStudent = user.getCurrentStudent();
        if (currentStudent != null) {
            sendChats();
            return EXIT;
        } else {
            wrongData();
        }

        return EXIT;
    }

    private void sendChats() throws TelegramApiException {
        sendMessageWithKeyboard(153, getLinkButton());
    }


    private ReplyKeyboard getLinkButton() {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        String link1 = currentStudent.getClassroom().getChatLinkChildren();
        String name1 = currentStudent.getClassroom().getNumber() + currentStudent.getClassroom().getLetter()+"(ученики)";

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setUrl(link1);
        button.setText(name1);
        row.add(button);


        rows.add(row);

        keyboard.setKeyboard(rows);

        return keyboard;
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
