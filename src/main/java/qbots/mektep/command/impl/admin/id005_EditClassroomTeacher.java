package qbots.mektep.command.impl.admin;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.checkerframework.checker.units.qual.C;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.Role;
import qbots.mektep.enums.WaitingType;
import qbots.mektep.model.standart.*;
import qbots.mektep.util.Const;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class id005_EditClassroomTeacher extends Command {

    int delMess;
//    Teacher teacher;
//    Classroom currentClassroom;
    private XSSFWorkbook  workbook        = new XSSFWorkbook();
    @Override
    public boolean execute() throws TelegramApiException {
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
                sendTemplate();
                return COMEBACK;
            case SET_DOCUMENT:
                deleteUpdateMess();
                if (hasDocument()){
                    try {
                        File file = bot.downloadFile(uploadFile(update.getMessage().getDocument().getFileId()));
                        List<Teacher> teachers =  importExcel(file);
                        sendMessage("Добавлено кл. рук:" + teachers.size());
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

        return EXIT;
    }

    private void wrongData() throws TelegramApiException {
        sendMessage(4);
    }

    private void sendTemplate() throws TelegramApiException {
        File file = new File("src\\main\\resources\\templates\\Шаблон классного руководителя.xlsx");
        try {
            InputFile inputFile = new InputFile(file);
            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(String.valueOf(chatId));
            sendDocument.setDocument(inputFile);
            sendDocument.setCaption(getText(11));
            delMess = bot.execute(sendDocument).getMessageId();

            waitingType = WaitingType.SET_DOCUMENT;

        } catch (Exception e) {
            sendMessage("Произошла ошибка");
            e.printStackTrace();
        }
    }




    private List<Teacher >  importExcel(File file1) throws IOException {

        List<Teacher > docRecipients = new ArrayList<>();
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



                Teacher teacher = teacherRepo.findByPhone(phone);
                if (teacher == null) {
                    teacher = new Teacher();
                    teacher.setPhone(phone);
                    teacher = teacherRepo.save(teacher);
                }

                List<Classroom > classrooms = getClassrooms(getStringValue(row, 3) ,teacher);

//                teacher.setMyClasses(classrooms);

                User user = usersRepo.findByPhone(phone);
                if (user!= null){
                    teacher.setUser(user);
                    user.addRole(Role.ROLE_CLASSROOM_TEACHER);
                    usersRepo.save(user);
                }

                teacherRepo.save(teacher);
                docRecipients.add(teacher);


            }
            catch (Exception e){
                e.printStackTrace();
            }

        }

        file.close();
        return docRecipients;
    }

    private List<Classroom> getClassrooms(String stringValue, Teacher teacher) {
        List<Classroom> classrooms = new ArrayList<>();
        for (String classroom : stringValue.split(",")){
            try {
                int number = getInt(classroom.split(" ")[0]);
                String letter = classroom.split(" ")[1];

                if (number >0 && number < 12 && letter.length() == 1) {
                    Classroom classroom1 = classroomRepo.findByNumberAndLetter(number, letter);
                    if (classroom1 == null) {
                        classroom1 = new Classroom();
                        classroom1.setNumber(number);
                        classroom1.setLetter(letter);

                    }
                    classroom1.setTeacher(teacher);
                    classroom1 = classroomRepo.save(classroom1);
                    classrooms.add(classroom1);
                }

            }catch (Exception ignored){}

        }

        return classrooms;
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
