package qbots.project.command.impl.admin;

import com.itextpdf.text.DocumentException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.project.command.Command;
import qbots.project.exceptions.ButtonNotFoundException;
import qbots.project.exceptions.CommandNotFoundException;
import qbots.project.exceptions.KeyboardNotFoundException;
import qbots.project.exceptions.MessageNotFoundException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

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

        if (isButton(307)){
            sendMessageWithKeyboard(38, 33);
            return COMEBACK;
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


}
