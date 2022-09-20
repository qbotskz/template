package qbots.mektep.command.impl.classroomTeacher;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.WaitingType;
import qbots.mektep.model.standart.Classroom;
import qbots.mektep.model.standart.Student;
import qbots.mektep.model.standart.Teacher;
import qbots.mektep.model.standart.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class id013_StudentsStatistics extends Command {
    User user;

    Teacher teacher;
    Classroom currentClassroom;
    private XSSFWorkbook  workbook        = new XSSFWorkbook();

    private int delMess;


    @Override
    public boolean execute() throws TelegramApiException {
        if (!isRegistered()){
            sendChooseLanguage();
            return EXIT;
        }

        switch (waitingType){
            case START:
                deleteUpdateMess();
                teacher = teacherRepo.findByUserChatId(chatId);
                if (teacher != null){
                    currentClassroom = teacher.getCurrentClassroom();
                    sendStat();
                }
                else {
                    sendMessage("У вас нет доступа");
                }
                return COMEBACK;

        }

        return EXIT;
    }

    private void sendStat() throws TelegramApiException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getText(128)).append(next);
        int i =1;
        for (Student student : currentClassroom.getStudents()) {
            if (student.getUser() == null) {
                stringBuilder.append(i++).append(") ").append(student.getFullName()).append(" ").append(student.getPhone()).append(next);
            }
        }

        sendMessage(stringBuilder.toString());
    }


    protected boolean isLong(String mess) {
        try {
            Long.parseLong(mess);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    protected long getLong(String mess) {
        try {
            return Long.parseLong(mess);
        } catch (Exception e) {
            return -1;
        }
    }

    private void wrongData() throws TelegramApiException {
        sendMessage(4);
    }

    private String getStringValue(Row row, int i) {
        try {
            return row.getCell(i).getStringCellValue();
        }catch (Exception e){
            return getNumericValue(row , i);
        }
    }

    private String getNumericValue(Row row, int i) {
        Double phoneDouble;
        try {
            phoneDouble = row.getCell(i).getNumericCellValue();
            return  String.valueOf(phoneDouble.longValue());
        } catch (Exception e) {
            return "";
        }
    }

}
