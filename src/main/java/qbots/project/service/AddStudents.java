//package qbots.mektep.service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFCellStyle;
//import org.apache.poi.xssf.usermodel.XSSFColor;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.telegram.telegrambots.bots.DefaultAbsSender;
//import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.InputFile;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import qbots.mektep.model.standart.Student;
//import qbots.mektep.repository.MessageRepo;
//import qbots.mektep.repository.TelegramBotRepositoryProvider;
//import qbots.mektep.repository.UsersRepo;
//
//import java.awt.Color;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@Slf4j
//public class AddStudents {
//
//    long chatId;
//    DefaultAbsSender bot;
//
//    private UsersRepo userRepository = TelegramBotRepositoryProvider.getUsersRepo();
//    private XSSFWorkbook  workbook        = new XSSFWorkbook();
//    private XSSFWorkbook  workbookClear        = new XSSFWorkbook();
//    //    private XSSFCellStyle style           = workbook.createCellStyle();
//    private Sheet         sheet;
//    private int           count;
//
//    private MessageRepo messageRepository = TelegramBotRepositoryProvider.getMessageRepo();
//
//
//
//    public List<Discount> fillDiscount(long chatId, DefaultAbsSender bot, File file) {
//        try {
//            this.chatId = chatId;
//            this.bot = bot;
//            List<Discount > regss = importExel(file);
////            sendExcess();
//            return regss;
//        } catch (Exception e) {
//            log.error("Can't create/send report", e);
//            try {
//                bot.execute(new SendMessage(String.valueOf(chatId), "Ошибка при импортировании"));
//            } catch (TelegramApiException ex) {
//                log.error("Can't send message", ex);
//            }
//            return new ArrayList<>();
//        }
//    }
//
//
//
//    private List<Discount >  importExel(File file1) throws IOException {
//
//        List<Discount > docRecipients = new ArrayList<>();
//        FileInputStream file = new FileInputStream(file1);
//        workbook = new XSSFWorkbook(file);
//        XSSFSheet sheet = workbook.getSheetAt(0);
//
//        String fullName= "";
//        String phone= "";
//
//
//        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
//
//            try {
//                Row row = sheet.getRow(i);
//                fullName = getStringValue(row, 1);
//                phone = getStringValue(row, 2);
//
//                Student student = new Student();
//                student.setFullName(fullName);
//                student.setPhone(phone);
//
//                Discount discount = new Discount();
//                discount.setCardNumber(cardNumber);
//                discount.setValue(percent);
//                .save(discount);
//
//
//                docRecipients.add(discount);
//            }
//            catch (Exception e){
//                e.printStackTrace();
//            }
//
//        }
//
//        file.close();
//        return docRecipients;
//    }
//
//    private Date getDateValue(Row row, int i) {
//        try {
//            return row.getCell(i).getDateCellValue();
//        }catch (Exception e){
//            return getDateStr(row, i);
//        }
//    }
//
//    private Date getDateStr(Row row, int i) {
//        try {
//            String date123 =  getStringValue(row, i);
//            DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
//            format.setLenient(true);
//            Date date741 = format.parse(date123);
//            return date741;
//        }catch (Exception e){
//            return null;
//        }
//    }
//
//    private String getStringValue(Row row, int i) {
//        try {
//            return row.getCell(i).getStringCellValue();
//        }catch (Exception e){
//            return getNumericValue(row , i);
//        }
//    }
//
//    private String getNumericValue(Row row, int i) {
//        Double phoneDouble;
//        try {
//            phoneDouble = row.getCell(i).getNumericCellValue();
//            return  String.valueOf(phoneDouble.longValue());
//        } catch (Exception e) {
//            return "";
//        }
//    }
//
//
//
//
//
//    private void            addInfo(List<List<String>> reports, int rowIndex) {
//        int cellIndex;
//        for (List<String> report: reports) {
//            sheet.createRow(++rowIndex);
//            insertToRow(rowIndex, report);
//        }
//        for (cellIndex = 0; cellIndex <= count; cellIndex++) {
//            sheet.autoSizeColumn(cellIndex);
//        }
//    }
//
//    private void            createTitle(XSSFCellStyle styleTitle, int rowIndex, List<String> title) {
//        sheet.createRow(rowIndex);
//        insertToRow(rowIndex, title);
//    }
//
//    private void            insertToRow(int row, List<String> cellValues) {
//        int cellIndex = 0;
//        for (String cellValue : cellValues) {
//            addCellValue(row, cellIndex++, cellValue);
//        }
//    }
//
//    private void            addCellValue(int rowIndex, int cellIndex, String cellValue) {
//        sheet.getRow(rowIndex).createCell(cellIndex).setCellValue(getString(cellValue));
//        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(setStyle());
//    }
//
//    private String          getString(String nullable) {
//        if (nullable == null) return "";
//        return nullable;
//    }
//
//    private XSSFCellStyle   setStyle() {
//        BorderStyle tittle          = BorderStyle.MEDIUM;
//        XSSFCellStyle styleTitle    = workbookClear.createCellStyle();
//        styleTitle.setWrapText(true);
//        styleTitle.setAlignment(HorizontalAlignment.CENTER);
//        styleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
//        styleTitle.setBorderTop(tittle);
//        styleTitle.setBorderBottom(tittle);
//        styleTitle.setBorderRight(tittle);
//        styleTitle.setBorderLeft(tittle);
//        styleTitle.setTopBorderColor(IndexedColors.BLACK.getIndex());
//        styleTitle.setRightBorderColor(IndexedColors.BLACK.getIndex());
//        styleTitle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//        styleTitle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//        styleTitle.setFillForegroundColor(new XSSFColor(new Color(0, 52, 94)));
//        return styleTitle;
//    }
//
//
//    private void                sendFile(long chatId, DefaultAbsSender bot, String fileName, String path) throws IOException, TelegramApiException {
//        File file = new File(path);
//        try (FileInputStream fileInputStream = new FileInputStream(file)) {
//            bot.execute(SendDocument.builder().chatId(String.valueOf(chatId)).document(new InputFile(fileInputStream, fileName)).build());
//        }
//        file.delete();
//    }
//
//
//
//}