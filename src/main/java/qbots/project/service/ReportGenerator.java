//package qbots.mektep.service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.poi.common.usermodel.HyperlinkType;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.*;
//import org.springframework.stereotype.Service;
//import org.telegram.telegrambots.bots.DefaultAbsSender;
//import org.telegram.telegrambots.meta.api.methods.GetFile;
//import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
//import org.telegram.telegrambots.meta.api.objects.InputFile;
//import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//
//import qbots.mektep.repository.TelegramBotRepositoryProvider;
//
//import java.awt.Color;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service
//public class ReportGenerator {
//
//    private Sheet sheets;
//    private Sheet sheet;
//    private XSSFWorkbook workbook = new XSSFWorkbook();
//    private XSSFCellStyle style = workbook.createCellStyle();
//    private XSSFCreationHelper  creationHelper  = workbook.getCreationHelper();
//    private int coloredColum;
//    String next = "\n";
//    String split = ";";
//    long chatId;
//
//    int messId;
//    private DefaultAbsSender bot;
//
//    //private AppealRepo appealRepo = TelegramBotRepositoryProvider.getAppealRepo();
//
//
//    public void message(long chatId, DefaultAbsSender bot, int del, Classroom classroom) throws TelegramApiException {
//        try {
//            messId = del;
//            this.bot = bot;
//            this.chatId = chatId;
//            message(chatId, classroom);
//        } catch (Exception e) {
//            e.printStackTrace();
//            bot.execute(new SendMessage(String.valueOf(chatId), "Ошибка при создании отчета"));
//        }
//    }
//
//    private void message(long chatId, Classroom classroom) throws TelegramApiException, IOException {
//        coloredColum = 99;
//        sheets = workbook.createSheet("Отчет");
//        sheet = workbook.getSheetAt(0);
//
//        List<Test> tests = TelegramBotRepositoryProvider.getTestRepo().findAllBy();
//
//        BorderStyle thin = BorderStyle.THIN;
//        short black = IndexedColors.BLACK.getIndex();
//        XSSFCellStyle styleTitle = setStyle(workbook, thin, black, style);
//        int rowIndex = 0;
//
//        createTitle(styleTitle, rowIndex, Arrays.asList((getTitle(tests, classroom)).split(";")));
//        List<List<String>> dorm = classroom.getStudents().stream().map(student -> {
//            List<String> list = new ArrayList<>();
//
//            list.add(student.getFullName());
//            for (Test test : tests){
//                list.add(getScore(test, student));
//            }
//
//            return list;
//        }).collect(Collectors.toList());
//
//        addInfo(dorm, rowIndex);
//
//        String fn = "Message";
//
//        sendFile(chatId, fn);
//    }
//
//
//
//
//
//    private String getTitle(List<Test> tests, Classroom classroom) {
//        StringBuilder str = new StringBuilder(classroom.getName() + split);
//        for (Test test : tests) {
//            str.append(test.getName(LanguageService.getLanguage(chatId))).append(split);
//        }
//        return str.toString();
//    }
//
//
//    private String getScore(Test test, Student student) {
//        List<StudentsAnswer> studentsAnswers = TelegramBotRepositoryProvider.getStudentsAnswerRepo().findAllByStudentAndQuestionTest(student, test);
//        int sc = 0;
//        for (StudentsAnswer studentsAnswer : studentsAnswers){
//            sc += studentsAnswer.getVariant().getPoint();
//        }
//        return String.valueOf(sc);
//    }
//
//
//
//
//
//
//    private String listUploadFiles(List<InputMedia> medias){
//        StringBuilder list = new StringBuilder();
//        if (medias  == null){
//            return list.toString();
//        }
//        for (InputMedia m:medias){
//            m.getMedia();
//            list.append(uploadFile(m.getMedia())).append("\n");
//        }
//        return list.toString();
//    }
//
//
//
//
//
//
//
//    private void getFiles(){
//
//    }
//    private void sendFile(long chatId,String filename) throws IOException, TelegramApiException {
//        String fileName = filename + ".xlsx";
//        String path = "C:\\" + fileName;
//        try (FileOutputStream stream = new FileOutputStream(path)) {
//            workbook.write(stream);
//        } catch (Exception e) {
//            log.error("Can't send File error: ", e);
//        }
//        sendFile(chatId, bot, fileName, path);
//
//    }
//    private void sendFile(long chatId, DefaultAbsSender bot, String fileName, String path) throws IOException, TelegramApiException {
//        try {
//            DeleteMessage deleteMessage = new DeleteMessage();
//            deleteMessage.setChatId(String.valueOf(chatId));
//            deleteMessage.setMessageId(messId);
//            bot.execute(deleteMessage);
//        }catch (Exception e){}
//
//        File file = new File(path);
//
//        try (FileInputStream fileInputStream = new FileInputStream(file)) {
//            deleteMessage(messId, chatId);
//            SendDocument sendDocument  = new SendDocument();
//            sendDocument.setChatId(String.valueOf(chatId));
//            sendDocument.setDocument(new InputFile(fileInputStream, fileName));
//            bot.execute(sendDocument);
//        }
//        file.delete();
//    }
//
//    private void deleteMessage(int messId ,long chatId) {
//        try {
//            DeleteMessage deleteMessage = new DeleteMessage();
//            deleteMessage.setChatId(String.valueOf(chatId));
//            deleteMessage.setMessageId(messId);
//            bot.execute(deleteMessage);
//        }catch (Exception ignored){}
//    }
//
//    private XSSFCellStyle setStyle(XSSFWorkbook workbook, BorderStyle thin, short black, XSSFCellStyle style) {
//        style.setWrapText(true);
//        style.setAlignment(HorizontalAlignment.CENTER);
//        style.setVerticalAlignment(VerticalAlignment.CENTER);
//        style.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
//        style.setBorderTop(thin);
//        style.setBorderBottom(thin);
//        style.setBorderRight(thin);
//        style.setBorderLeft(thin);
//        style.setTopBorderColor(black);
//        style.setRightBorderColor(black);
//        style.setBottomBorderColor(black);
//        style.setLeftBorderColor(black);
//        style.getFont().setBold(true);
//        BorderStyle tittle = BorderStyle.MEDIUM;
//
//        XSSFFont titleFont = workbook.createFont();
//        titleFont.setBold(true);
//        titleFont.setColor(black);
//        titleFont.setFontHeight(10);
//
//        XSSFCellStyle styleTitle = workbook.createCellStyle();
//        styleTitle.setWrapText(true);
//        styleTitle.setAlignment(HorizontalAlignment.CENTER);
//        styleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
//        styleTitle.setBorderTop(tittle);
//        styleTitle.setBorderBottom(tittle);
//        styleTitle.setBorderRight(tittle);
//        styleTitle.setBorderLeft(tittle);
//        styleTitle.setTopBorderColor(black);
//        styleTitle.setRightBorderColor(black);
//        styleTitle.setBottomBorderColor(black);
//        styleTitle.setLeftBorderColor(black);
//        styleTitle.setFont(titleFont);
//        style.setFillForegroundColor(new XSSFColor(new Color(0, 94, 94)));
//        return styleTitle;
//    }
//
//    private String uploadFile(String fileId) {
//        if (fileId == null)
//            return "";
//
//        Objects.requireNonNull(fileId);
//        GetFile getFile = new GetFile();
//        getFile.setFileId(fileId);
//        try {
//            org.telegram.telegrambots.meta.api.objects.File file = bot.execute(getFile);
//            return file.getFileUrl(TelegramBotRepositoryProvider.getPropertiesRepo().findById(2).get().getValue());
//            //return "https://api.telegram.org/file/bot" + "5537741360:AAEuJEJ72gBxiBOrGmCq2VJaEoeh2ahTEnY" + "/" + uploadFile2(fileId, bot);
//        } catch (TelegramApiException e) {
//            throw new IllegalMonitorStateException();
//        }
//    }
//
//    private String uploadFile2(String fileId, DefaultAbsSender bot) {
//        //Bot bot1 = new Bot();
//
//        Objects.requireNonNull(fileId);
//
//        GetFile getFile = new GetFile();
//        getFile.setFileId(fileId);
//
//        try {
//            org.telegram.telegrambots.meta.api.objects.File file = bot.execute(getFile);
////            bot.execute(message);
//            return file.getFilePath();
//        } catch (TelegramApiException e) {
//            System.out.println(e.getMessage());
//            throw new IllegalMonitorStateException();
//        }
//    }
//
//    private void createTitle(XSSFCellStyle styleTitle, int rowIndex, List<String> title) {
//        sheets.createRow(rowIndex);
//        insertToRow(rowIndex, title, styleTitle);
//    }
//    private void insertToRow(int row, List<String> cellValues, CellStyle cellStyle) {
//        int cellIndex = 0;
//        for (String cellValue : cellValues) {
//            if (cellValue != null && cellValue.contains("https://api.telegram.org"))
//                addCellValueLink(row, cellIndex++, cellValue, cellStyle);
//            else if (cellIndex == coloredColum && row != 0){
//                addCellValueColored(row, cellIndex++, cellValue, cellStyle, (cellValue == null ? IndexedColors.RED.getIndex() : IndexedColors.GREEN.getIndex()) );
//            }else  {
//                addCellValue(row, cellIndex++, cellValue, cellStyle);
//            }
//        }
//    }
//    private void addCellValue(int rowIndex, int cellIndex, String cellValue, CellStyle cellStyle) {
//
//        sheets.getRow(rowIndex).createCell(cellIndex).setCellValue(getString(cellValue));
//        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(cellStyle);
//    }
//
//    private void addCellValueColored(int rowIndex, int cellIndex, String cellValue, CellStyle cellStyle, short indexedColors_COLOR_NAME_getIndex) {
//
//        sheets.getRow(rowIndex).createCell(cellIndex).setCellValue(getString(cellValue));
//
//        CellStyle newStyle = workbook.createCellStyle();
//        newStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        newStyle.setFillForegroundColor(indexedColors_COLOR_NAME_getIndex);
//
//        BorderStyle thin = BorderStyle.THIN;
//        newStyle.setBorderTop(thin);
//        newStyle.setBorderBottom(thin);
//        newStyle.setBorderRight(thin);
//        newStyle.setBorderLeft(thin);
//        short black = IndexedColors.BLACK.getIndex();
//        newStyle.setTopBorderColor(black);
//        newStyle.setRightBorderColor(black);
//        newStyle.setBottomBorderColor(black);
//        newStyle.setLeftBorderColor(black);
//
//        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(newStyle);
//    }
//
//    private void            addCellValueLink(int rowIndex, int cellIndex, String cellValue, CellStyle cellStyle) {
//        try {
//            XSSFHyperlink link = creationHelper.createHyperlink(HyperlinkType.URL);
//            link.setAddress(cellValue);
//            sheets.getRow(rowIndex).createCell(cellIndex).setHyperlink(link);
//            sheets.getRow(rowIndex).getCell(cellIndex).setCellValue("Ссылка для скачивания");
//            sheets.getRow(rowIndex).getCell(cellIndex).setCellStyle(cellStyle);
////            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(cellStyle);
//
//        } catch (Exception e) {
//            if (e.getMessage().contains("Address of hyperlink must be a valid URI"))
//                sheets.getRow(rowIndex).getCell(cellIndex).setCellValue(" ");
//        }
//    }
//
//    private String getString(String nullable) {
//        if (nullable == null) return "";
//        return nullable;
//    }
//
//    private void addInfo(List<List<String>> reports, int rowIndex) {
//        for (List<String> report : reports) {
//            sheets.createRow(++rowIndex);
//            insertToRow(rowIndex, report, style);
//
//        }
//        for (int i = 1; i <= 30; i++) {
//            sheets.autoSizeColumn(i);
//        }
//    }
//
//    private void addInfoWithColor(List<List<String>> reports, int rowIndex) {
//        for (List<String> report : reports) {
//            sheets.createRow(++rowIndex);
//            insertToRow(rowIndex, report, style);
//
//        }
//        for (int i = 1; i <= 30; i++) {
//            sheets.autoSizeColumn(i);
//        }
//    }
//
//}
