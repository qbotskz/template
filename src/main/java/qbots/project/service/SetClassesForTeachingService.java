//package qbots.mektep.service;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFCellStyle;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Value;
//import org.telegram.telegrambots.bots.DefaultAbsSender;
//import org.telegram.telegrambots.meta.api.methods.GetFile;
//import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
//import org.telegram.telegrambots.meta.api.objects.InputFile;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import qbots.mektep.command.Command;
//import qbots.mektep.repository.TelegramBotRepositoryProvider;
//
//import java.io.File;
//import java.util.*;
//
//public class SetClassesForTeachingService {
//
//    long chatId;
//    DefaultAbsSender bot;
//
//    XSSFWorkbook workbook = new XSSFWorkbook();
//    XSSFCellStyle style = workbook.createCellStyle();
//    XSSFCellStyle titleStyle = workbook.createCellStyle();
//    Sheet sheets;
//    Sheet sheet;
//
//
//    @Getter
//    @Setter
//    @Value(value = "${path-for-report}")
//    private String path;
//
//    public SetClassesForTeachingService(long chatId, DefaultAbsSender bot) {
//        this.chatId = chatId;
//        this.bot = bot;
//    }
//
//    public  void saveTeachers (String fileId) {
//        int messId = sendMessage(22);
//
//        try {
//            File file = bot.downloadFile(uploadFile(fileId, bot));
//            XSSFWorkbook xssfWorkbook;
//            try {
//                xssfWorkbook = new XSSFWorkbook(file);
//            } catch (Exception e) {
//                sendMessage(704);
//                return;
//            }
//            int activeSheetIx = xssfWorkbook.getActiveSheetIndex();
//            String sheetName = xssfWorkbook.getSheetName(activeSheetIx);
//            Sheet sheet = xssfWorkbook.getSheet(sheetName);
//            if (sheet == null) {
//                sendMessage(String.format(getText(705) ,sheetName));
//                return;
//            }
//
//            StringBuilder notAdded = new StringBuilder("Не добавленные:\n");
//
//
//            for (int index = 1; index < sheet.getLastRowNum() + 1; index++) {
//                Row row = sheet.getRow(index);
//                if (row == null || row.getPhysicalNumberOfCells() == 0) {
//                    continue;
//                }
//
//
//
//
//                String fullName = getValue(row, 0);
//                String phoneNumber = Command.formatPhone(getValue(row, 2));
//                if (phoneNumber == null || phoneNumber.equals("")){
//                    continue;
//                }
//
//                phoneNumber = phoneNumber.replaceAll(" ", "");
//                Teacher teacher = TelegramBotRepositoryProvider.getTeacherRepo().findByPhone(phoneNumber);
//
//                if(teacher == null){
//                    teacher = new Teacher();
//                    teacher.setPhone(phoneNumber);
////                    teacher.setFullName(fullName);
//                    teacher = TelegramBotRepositoryProvider.getTeacherRepo().save(teacher);
//                }
//
//                teacher.setClassesForTeaching(new ArrayList<>());
//
//                for (int i = 3; (getValue(row, i) != null && !getValue(row, i).equals("")); i++){
//                    Classroom classroom = getKlass(getValue(row, i));
//                    if (classroom != null){
//                        teacher.addClassesForTeaching(classroom);
//                    }
//                    else {
//                        notAdded.append(fullName).append(": ").append(getValue(row, i)).append("\n");
//                    }
//                }
//                teacher = TelegramBotRepositoryProvider.getTeacherRepo().save(teacher);
//
//                String departmentName = getValue(row, 1);
//                if (departmentName != null && !departmentName.equals("")) {
//                    Department department = TelegramBotRepositoryProvider.getDepartmentRepo().findByName(departmentName);
//                    if (department == null){
//                        department = new Department();
//                        department.setName(departmentName);
//                    }
//                    department.addTeacher(teacher);
//                    TelegramBotRepositoryProvider.getDepartmentRepo().save(department);
//                }
//
//
//            }
//
//            deleteMess(messId);
//            sendMessage("Учителя успешно добавились!");
//
//
//            if (notAdded.toString().length() > 17){
//                sendMessage(notAdded.toString());
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//            sendMessage("Ошибка файла!");
//        }
//    }
//
//    private Classroom getKlass(String value) {
//        if (value!= null && value.split(" ").length == 2){
//            String num = value.split(" ")[0].trim();
//            int numInt = getIntMy(num);
//            String let = value.split(" ")[1].trim();
//            return TelegramBotRepositoryProvider.getClassroomRepo().findByNumberAndLetter(numInt, let);
//        }
//        return null;
//    }
//
//    private  int getIntMy(String parseInt) {
//        try {
//            return Integer.parseInt(parseInt);
//        }catch (Exception e){
//            return -1;
//        }
//    }
//
//    private  String getValue(Row row, int numberCell) {
//        try {
//            Cell cell = row.getCell(numberCell);
//            cell.setCellType(CellType.STRING);
//            return cell.getStringCellValue();
//        }catch (Exception e){
//            return null;
//        }
//    }
//
//    private int   sendMessage(int messId) {
//        try {
//            return bot.execute(SendMessage.builder().chatId(String.valueOf(chatId)).parseMode("html").text(getText(messId)).build()).getMessageId();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            return 0;
//
//        }
//
//    }
//
//    private int   sendMessage(String mess) {
//        try {
//            return bot.execute(SendMessage.builder().chatId(String.valueOf(chatId)).parseMode("html").text(mess).build()).getMessageId();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            return 0;
//
//        }
//
//    }
//
//
//    private  String getText(int messId){
//
//        return TelegramBotRepositoryProvider.getMessageRepo().findByIdAndLanguageId(messId, LanguageService.getLanguage(chatId).getId()).getName();
//    }
//
//    private   void deleteMess(int messId){
//        try {
//            bot.execute(DeleteMessage.builder().chatId(String.valueOf(chatId)).messageId(messId).build());
//        }catch (Exception ignored){}
//    }
//
//    private  String uploadFile(String fileId, DefaultAbsSender bot) {
//        Objects.requireNonNull(fileId);
//        GetFile getFile = new GetFile();
//        getFile.setFileId(fileId);
//        try {
//            org.telegram.telegrambots.meta.api.objects.File file = bot.execute(getFile);
//            return file.getFilePath();
//        } catch (TelegramApiException e) {
//            throw new IllegalStateException(e);
//        }
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//    public void sendFileForSetLink(){
//        try {
//            SendDocument sendDocument = new SendDocument();
//            sendDocument.setDocument(new InputFile(new File("src/main/resources/templates/Шаблон списка учителей.xlsx")));
//            sendDocument.setChatId(String.valueOf(chatId));
//            sendDocument.setCaption(getText(21));
//            bot.execute(sendDocument);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//}
