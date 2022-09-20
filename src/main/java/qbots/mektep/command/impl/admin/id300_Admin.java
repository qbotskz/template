package qbots.mektep.command.impl.admin;

import com.itextpdf.text.DocumentException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.Role;
import qbots.mektep.exceptions.ButtonNotFoundException;
import qbots.mektep.exceptions.CommandNotFoundException;
import qbots.mektep.exceptions.KeyboardNotFoundException;
import qbots.mektep.exceptions.MessageNotFoundException;
import qbots.mektep.model.standart.RequestFromParent;
import qbots.mektep.model.standart.Student;
import qbots.mektep.model.standart.User;
import qbots.mektep.util.Const;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class id300_Admin extends Command {



    @Override
    public boolean execute() throws TelegramApiException, IOException, SQLException, FileNotFoundException, MessageNotFoundException, KeyboardNotFoundException, ButtonNotFoundException, CommandNotFoundException, DocumentException {

        if (!isRegistered() ){
            sendChooseLanguage();
            return EXIT;
        }

        if (!isAdmin()){
            sendChooseLanguage();
            return EXIT;
        }

        switch (waitingType){
             case START:
                 if (isButton(300)){
                     sendAdminPanel();
                 }
                 return COMEBACK;
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
