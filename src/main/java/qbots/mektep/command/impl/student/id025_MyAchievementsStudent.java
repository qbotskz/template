package qbots.mektep.command.impl.student;

import com.itextpdf.text.DocumentException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.exceptions.ButtonNotFoundException;
import qbots.mektep.exceptions.CommandNotFoundException;
import qbots.mektep.exceptions.KeyboardNotFoundException;
import qbots.mektep.exceptions.MessageNotFoundException;
import qbots.mektep.model.standart.AppealToPsych;
import qbots.mektep.model.standart.Student;
import qbots.mektep.model.standart.StudentsAchievements;
import qbots.mektep.model.standart.User;
import qbots.mektep.util.DateUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class id025_MyAchievementsStudent extends Command {

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
                sendAchievements();

                return EXIT;
            } else {
                wrongData();
            }


        return EXIT;
    }

    private void sendAchievements() throws TelegramApiException {
        List<StudentsAchievements> studentsAchievements = studentsAchievementsRepo.findAllByStudent(currentStudent);
        if (studentsAchievements.size() == 0){
            sendMessage(408);
        }
        else {
            for (StudentsAchievements achievement : studentsAchievements){
                sendPhoto(achievement.getPhoto(), chatId);
                sendMessage(getInfo(achievement));
            }
        }
    }

    private String getInfo(StudentsAchievements achievement) {

        return getText(409)+" " + DateUtil.getDayDate(achievement.getSentDate())+next+
                getText(410) + " " + achievement.getPlace()+next+
                getText(411)+ " " + achievement.getSubject()+next+
                getText(412) + " " + achievement.getNameEvent()+next+
                getText(413) + " " + achievement.getLevelEvent()+next;
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
