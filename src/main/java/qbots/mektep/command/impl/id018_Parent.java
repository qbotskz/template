//package qbots.mektep.command.impl;
//
//import com.itextpdf.text.DocumentException;
//import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
//import org.telegram.telegrambots.meta.api.objects.InputFile;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import qbots.mektep.command.Command;
//import qbots.mektep.enums.WaitingType;
//import qbots.mektep.exceptions.ButtonNotFoundException;
//import qbots.mektep.exceptions.CommandNotFoundException;
//import qbots.mektep.exceptions.KeyboardNotFoundException;
//import qbots.mektep.exceptions.MessageNotFoundException;
//import qbots.mektep.util.DateKeyboard;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.Date;
//
//public class id018_Parent extends Command {
//
//    private DateKeyboard dateKeyboard;
//    private String text;
//    private Date start;
//    private Date end;
//    private int editCalendar;
//
//    @Override
//    public boolean execute() throws TelegramApiException, IOException, SQLException, FileNotFoundException, MessageNotFoundException, KeyboardNotFoundException, ButtonNotFoundException, CommandNotFoundException, DocumentException {
//        switch (waitingType){
//
//            case START:
//                if (isButton(10001)){
//                    waitingType = WaitingType.START_PARENT;
//                    return COMEBACK;
//                }
//
//            case START_PARENT:
//                if (isButton(201)){
//                    sendMessageWithKeyboard("", 201);
//                    waitingType = WaitingType.DATE_OF_FLUR;
//                    return COMEBACK;
//                } else if (isButton(202)) {
//                    sendMessageWithKeyboard("",202);
//                    waitingType = WaitingType.SOCIAL_PASSPORT;
//                    return COMEBACK;
//                }else if (isButton(203)) {
//                    sendMessageWithKeyboard("", 203);
//                    waitingType = WaitingType.ACHIEVMENTS;
//                    return COMEBACK;
//                }
////                } else if (isButton(205)) {
////                    sendMessageWithKeyboard("",205);
////                    waitingType = WaitingType.PARENTS;
////                    return COMEBACK;
////                }
////            case PARENTS:
////                if (hasCallbackQuery()){
////                    if (isButton(206)){
////
////                    }
////                }
//            case ACHIEVMENTS:
//            case SOCIAL_PASSPORT:
//            case DATE_OF_FLUR:
//                if (hasCallbackQuery()) {
//                    if (isButton(204)){
//                        dateKeyboard = new DateKeyboard();
//                        text = "Выберите пожалуйста начальную дату";
//                        editCalendar = sendMessageWithKeyboard(text, dateKeyboard.getWeekCalendarKeyboard());
//                        waitingType = WaitingType.DATE_START;
//                        return COMEBACK;
//                    }
//                }
//            case DATE_START:
//                if (hasCallbackQuery() && dateKeyboard.isNext(updateMessageText)) {
//                    editMessageWithKeyboard(text,chatId, updateMessageId, dateKeyboard.getWeekCalendarKeyboard());
//                } else if (hasCallbackQuery() && dateKeyboard.getDateDate(updateMessageText) != null) {
//                    start = dateKeyboard.getDateDate(updateMessageText);
//                    start.setHours(0);
//                    start.setMinutes(0);
//                    start.setSeconds(0);
//                    text = "Выберите пожалуйста конечную дату";
//                    sendMessageWithKeyboard(text, dateKeyboard.getWeekCalendarKeyboard());
//                    waitingType = WaitingType.DATE_END;
//                }
//                return COMEBACK;
//            case DATE_END:
//                if (hasCallbackQuery() && dateKeyboard.isNext(updateMessageText)) {
//                    editMessageWithKeyboard(text,chatId, updateMessageId, dateKeyboard.getWeekCalendarKeyboard());
//                } else if (hasCallbackQuery() && dateKeyboard.getDateDate(updateMessageText) != null) {
//                    end = dateKeyboard.getDateDate(updateMessageText);
//                    end.setHours(0);
//                    end.setMinutes(0);
//                    end.setSeconds(0);
//                    sendMessage("Пожалуйста отправьте фотографию флюорографии");
//                    waitingType = WaitingType.SEND_FILE;
//                    return EXIT;
//                }
//                return COMEBACK;
//            case SEND_FILE:
//                if (update.hasMessage() && update.getMessage().hasPhoto()) {
//
//                    SendPhoto sd = new SendPhoto();
//                    sd.setChatId(String.valueOf(chatId));
//                    sd.setPhoto(new InputFile(updateMessagePhone));
//                    bot.execute(sd);
//                    sendMessage("Успешно отправлено");
//                    waitingType = WaitingType.CHATS;
//                    return COMEBACK;
//                }else {
//                    sendMessage("wtf!!");
//                }
//            case CHATS:
//                if (hasCallbackQuery()){
//                    if (isButton(206)){
//                        sendMessageWithKeyboard("",206);
//                        waitingType = WaitingType.REQUEST;
//                        return COMEBACK;
//                    }else if (isButton(207)){
//                        sendMessageWithKeyboard("",206);
//                        waitingType = WaitingType.REQUEST;
//                        return COMEBACK;
//                    }
//                }
//
//        }
//        return EXIT;
//    }
//}
