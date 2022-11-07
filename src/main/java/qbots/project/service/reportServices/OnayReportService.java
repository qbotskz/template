//package qbots.mektep.service.reportServices;
//
//import lombok.Getter;
//import lombok.Setter;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.poi.common.usermodel.HyperlinkType;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.*;
//import org.springframework.beans.factory.annotation.Value;
//import org.telegram.telegrambots.bots.DefaultAbsSender;
//import org.telegram.telegrambots.meta.api.methods.GetFile;
//import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
//import org.telegram.telegrambots.meta.api.objects.InputFile;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import qbots.mektep.enums.Language;
//import qbots.mektep.exceptions.MessageNotFoundException;
//import qbots.mektep.repository.TelegramBotRepositoryProvider;
//import qbots.mektep.service.LanguageService;
//import qbots.mektep.util.Const;
//import qbots.mektep.util.DateUtil;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Slf4j
//public class OnayReportService extends Thread {
//
//
//    private XSSFWorkbook workbook = new XSSFWorkbook();
//    private XSSFCellStyle style = workbook.createCellStyle();
//    private XSSFCellStyle titleStyle = workbook.createCellStyle();
//    private Sheet sheets;
//    private Sheet sheet;
//    private Language currentLanguage = Language.ru;
//    private XSSFCreationHelper creationHelper = workbook.getCreationHelper();
//    private int index = 0;
//
//
//    @Getter
//    @Setter
//    @Value(value = "${path-for-report}")
//    private String path;
//
//    long chatId;
//    DefaultAbsSender bot;
//    List<Onay> onays;
//
//    public OnayReportService(long chatId, DefaultAbsSender bot, List<Onay> onays) {
//        this.chatId = chatId;
//        this.bot = bot;
//        this.onays = onays;
//    }
//
//    @Override
//    public void run() {
//        int preview                         = sendMessage("Отчет подготавливается...");
//        currentLanguage = LanguageService.getLanguage(chatId);
//        try {
//            if (onays.size() == 0) {
//                bot.execute(new SendMessage(String.valueOf(chatId), "Данных нет"));
//            } else {
//                sendReport(chatId, bot, onays);
//            }
//        } catch (Exception e) {
//            log.error("Can't create/send report", e);
//            sendMessage("Ошибка при создании отчета");
//        }
//        deleteMessage(chatId, preview);
//
//    }
//
//    public  void     deleteMessage(long chatId, int messageId) {
//        try {
//            bot.execute(new DeleteMessage(String.valueOf(chatId), messageId));
//        } catch (TelegramApiException ignored) {}
//    }
//
//    private int sendMessage(String s) {
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(String.valueOf(chatId));
//        sendMessage.setText(s);
//
//        sendMessage.setParseMode("html");
//
//        try {
//            return bot.execute(sendMessage).getMessageId();
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//
//
//    private void sendReport(long chatId, DefaultAbsSender bot, List<Onay> onays) throws MessageNotFoundException {
//        sheets = workbook.createSheet("Отчет");
//        sheet = workbook.getSheetAt(0);
//        BorderStyle thin = BorderStyle.THIN;
//        short black = IndexedColors.BLACK.getIndex();
//        XSSFCellStyle styleTitle = setStyle(workbook, thin, black, style);
//        XSSFCellStyle titleStyle = titleStyle(workbook, thin, black, style);
//        int rowIndex = 0;
//        try {
//            createTitle(titleStyle, rowIndex, Arrays.asList(("№;Класс;ФИО;Дата рождения;Удостоверение личности;3х4 фото;Дата заявки").split(Const.SPLIT)));
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//
//        List<List<String>> info = onays.stream().map(onay -> {
//            List<String> list = new ArrayList<>();
//            index++;
//            list.add(String.valueOf(index));
//            list.add(onay.getStudent().getClassroom().getName());
//            list.add(onay.getFullName());
//            list.add(onay.getBirthday());
//            list.add(getFileLink(onay.getCard()));
//            list.add(getFileLink(onay.getPhoto3x4()));
//            list.add(DateUtil.getDayDate(onay.getSentDate()));
//
//
//           return list;
//        }).collect(Collectors.toList());
//
//        addInfo(info, rowIndex);
//
//        try {
//            sendFile(chatId, bot);
//        } catch (TelegramApiException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String getAns(String answerText) {
//        if(answerText == null)
//            return " ";
//        return answerText;
//    }
//
//    private String getFileLink(String fileId) {
//        try {
//            if (fileId != null) {
//                return Optional.of("https://api.telegram.org/file/bot" + TelegramBotRepositoryProvider.getPropertiesRepo().findFirstById(2).getValue() + "/" + uploadFile(fileId)).orElse(" - ");
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        return " ";
//    }
//
//    private String uploadFile(String fileId) {
//        Objects.requireNonNull(fileId);
//        GetFile getFile = GetFile.builder().fileId(fileId).build();
//        try {
//            org.telegram.telegrambots.meta.api.objects.File file = bot.execute(getFile);
//            return file.getFilePath();
//        } catch (TelegramApiException e) {
//            throw new IllegalMonitorStateException();
//        }
//    }
//
//    private void insertToRowURL(int row, String cellValue, int cellIndex, CellStyle cellStyle, Sheet sheet) {
//        addCellValueLink(row, cellIndex, cellValue, cellStyle, sheet);
//    }
//
//    private void addCellValueLink(int rowIndex, int cellIndex, String cellValue, CellStyle cellStyle, Sheet sheets) {
//        try {
//            XSSFHyperlink link = creationHelper.createHyperlink(HyperlinkType.URL);
//            link.setAddress(cellValue);
//            sheets.getRow(rowIndex).createCell(cellIndex).setHyperlink(link);
//            sheets.getRow(rowIndex).getCell(cellIndex).setCellStyle(cellStyle);
//            sheets.getRow(rowIndex).getCell(cellIndex).setCellValue(cellValue);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            if (e.getMessage().contains("Address of hyperlink must be a valid URI"))
//                sheets.getRow(rowIndex).getCell(cellIndex).setCellValue(" ");
//        }
//    }
//
//    private void addInfo(List<List<String>> reports, int rowIndex) throws MessageNotFoundException {
//
//        for (List<String> report : reports) {
//            sheets.createRow(++rowIndex);
//            addLinks(report, rowIndex);
//            report = removeLinks(report);
//            insertToRow(rowIndex, report, style);
//        }
//        for (int index = 0; index < 20; index++) {
//            sheet.autoSizeColumn(index);
//        }
//    }
//
//    private List<String> removeLinks(List<String> report) {
//        List<String> info = new ArrayList<>();
//        for (String str : report){
//            if (str.contains("https://"))
//                info.add("Ссылка");
//            else info.add(str);
//        }
//        return info;
//    }
//
//    private void addLinks(List<String> report, int rowIndex) {
//        for (int i = 0; i <report.size() ; i++) {
//            if (report.get(i).contains("https://")){
//                insertToRowURL(rowIndex, report.get(i), i, style, sheet);
//            }
//        }
//    }
//
//
//    private void insertToRow(int row, List<String> cellValues, CellStyle cellStyle) {
//        int cellIndex = 0;
//        for (String cellValue : cellValues) {
//            addCellValue(row, cellIndex++, cellValue, cellStyle);
//        }
//
//    }
//
//    private void addCellValue(int rowIndex, int cellIndex, String cellValue, CellStyle cellStyle) {
//        sheets.getRow(rowIndex).createCell(cellIndex).setCellValue(getString(cellValue));
//        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(cellStyle);
//    }
//
//    private void createTitle(XSSFCellStyle styleTitle, int rowIndex, List<String> title) {
//        sheets.createRow(rowIndex);
//        insertToRow(rowIndex, title, styleTitle);
//    }
//
//    private String getString(String nullable) {
//        if (nullable == null) return "";
//        return nullable;
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
//        BorderStyle tittle = BorderStyle.MEDIUM;
//
//        XSSFFont titleFont = workbook.createFont();
//        titleFont.setFontHeight(10);
//        titleFont.setBold(true);
//        titleFont.setColor(black);
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
//        styleTitle.setFont(titleFont);
//        styleTitle.setLeftBorderColor(black);
//        style.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 52, 94)));
//        return styleTitle;
//    }
//
//    private XSSFCellStyle titleStyle(XSSFWorkbook workbook, BorderStyle thin, short black, XSSFCellStyle style) {
//        XSSFFont font = workbook.createFont();
//        font.setFontHeightInPoints((short) 12);
//        font.setBold(true);
//
//
//        style.setWrapText(true);
//        style.setAlignment(HorizontalAlignment.CENTER);
//        style.setVerticalAlignment(VerticalAlignment.CENTER);
//
//        style.setBorderTop(thin);
//        style.setBorderBottom(thin);
//        style.setBorderRight(thin);
//        style.setBorderLeft(thin);
//        style.setTopBorderColor(black);
//        style.setRightBorderColor(black);
//        style.setBottomBorderColor(black);
//        style.setLeftBorderColor(black);
//        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//
//        BorderStyle tittle = BorderStyle.THIN;
//        XSSFCellStyle styleTitle = workbook.createCellStyle();
//        styleTitle.setWrapText(true);
//        styleTitle.setAlignment(HorizontalAlignment.CENTER);
//        styleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
//
//        styleTitle.setBorderTop(tittle);
//        styleTitle.setBorderBottom(tittle);
//        styleTitle.setBorderRight(tittle);
//        styleTitle.setBorderLeft(tittle);
//
//        styleTitle.setTopBorderColor(black);
//        styleTitle.setRightBorderColor(black);
//        styleTitle.setBottomBorderColor(black);
//        styleTitle.setLeftBorderColor(black);
//        styleTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        styleTitle.setFont(font);
//
//
//        styleTitle.setFillForegroundColor(new XSSFColor(new java.awt.Color(80, 250, 49, 233)));
//        style.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 255)));
//        return styleTitle;
//    }
//
//    private void sendFile(long chatId, DefaultAbsSender bot) throws IOException, TelegramApiException {
//        String fileName = "Отчет.xlsx";
//        String path = getPath() + fileName;
//        path += new Date().getTime();
//        try (FileOutputStream stream = new FileOutputStream(path)) {
//            workbook.write(stream);
//        } catch (IOException e) {
//            log.error("Can't send File error: ", e);
//        }
//        sendFile(chatId, bot, fileName, path);
//    }
//
//    private void sendFile(long chatId, DefaultAbsSender bot, String fileName, String path) throws TelegramApiException {
//        File file = new File(path);
//        try (FileInputStream fileInputStream = new FileInputStream(file)) {
//            bot.execute(SendDocument.builder().chatId(String.valueOf(chatId)).document(new InputFile(fileInputStream, fileName)).build());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            file.delete();
//        }
//    }
//}
