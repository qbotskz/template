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
import qbots.mektep.util.ButtonsLeaf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class id011_AddClass extends Command {
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
                    sendTemplate();
                }
                else {
                    sendMessage("У вас нет доступа");
                }
                return COMEBACK;
            case SET_DOCUMENT:
                deleteUpdateMess();
                if (hasDocument()){
                    try {
                        File file = bot.downloadFile(uploadFile(update.getMessage().getDocument().getFileId()));
                        List<Student> students =  importExcel(file);
                        sendMessage("Добавлено учеников:" + students.size());
                        return EXIT;
                    } catch (IOException e) {
                        wrongData();
                        sendTemplate();
                        e.printStackTrace();
                    }
                }
                else {
                    wrongData();
                    sendTemplate();
                }
                return COMEBACK;
        }

        return COMEBACK;
    }

    private void sendTemplate() throws TelegramApiException {
        File file = new File("src\\main\\resources\\templates\\Пример отправки учеников .xlsx");
        try {
            InputFile inputFile = new InputFile(file);
            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(String.valueOf(chatId));
            sendDocument.setDocument(inputFile);
            sendDocument.setCaption(getText(127) + currentClassroom.getNumber()+ currentClassroom.getLetter());
            delMess = bot.execute(sendDocument).getMessageId();

            waitingType = WaitingType.SET_DOCUMENT;

        } catch (Exception e) {
            sendMessage("Произошла ошибка");
            e.printStackTrace();
        }
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

    private List<Student >  importExcel(File file1) throws IOException {

        List<Student > docRecipients = new ArrayList<>();
        FileInputStream file = new FileInputStream(file1);
        workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);

        String fullName= "";
        String phone= "";


        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {

            try {
                Row row = sheet.getRow(i);
                fullName = getStringValue(row, 1);
                phone = getStringValue(row, 2);

                Student student = new Student();
                student.setFullName(fullName);
                student.setPhone(phone);
                student.setClassroom(currentClassroom);
                Student studentForCheck = studentRepo.findByPhoneAndFullName(phone, fullName);
                if (studentForCheck == null) {
                    User user = usersRepo.findByPhone(phone);
                    if (user != null) {
                        student.setUser(user);
                    }
                    studentRepo.save(student);
                    docRecipients.add(student);
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }

        }

        file.close();
        return docRecipients;
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
