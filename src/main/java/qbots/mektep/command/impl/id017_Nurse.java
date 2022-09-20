//package qbots.mektep.command.impl;
//
//import com.itextpdf.text.DocumentException;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import qbots.mektep.command.Command;
//import qbots.mektep.enums.WaitingType;
//import qbots.mektep.exceptions.ButtonNotFoundException;
//import qbots.mektep.exceptions.CommandNotFoundException;
//import qbots.mektep.exceptions.KeyboardNotFoundException;
//import qbots.mektep.exceptions.MessageNotFoundException;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.sql.SQLException;
//
//public class id017_Nurse extends Command {
//    @Override
//    public boolean execute() throws TelegramApiException, IOException, SQLException, FileNotFoundException, MessageNotFoundException, KeyboardNotFoundException, ButtonNotFoundException, CommandNotFoundException, DocumentException {
//        switch (waitingType){
//            case NURSE:
//                if(isButton(301)){
//                    sendMessageWithKeyboard("",301);
//                    waitingType = WaitingType.OTCHET;
//                    return COMEBACK;
//                }
//                else if (isButton(307)){
//                    sendMessageWithKeyboard("",307);
//                    waitingType = WaitingType.MAILING_LIST;
//                    return COMEBACK;
//                }
//            case MAILING_LIST:
//                if (hasCallbackQuery()){
//                    if (isButton(309)){
//                        sendMessageWithKeyboard("",309);
//                        waitingType = WaitingType.CHOOSE_ALL;
//                        return COMEBACK;
//                    } else if (isButton(310)) {
//                        sendMessageWithKeyboard("",309);
//                        waitingType = WaitingType.CHOOSE;
//                        return COMEBACK;
//                    }
//                }
//            case CHOOSE_ALL:
//                if (hasCallbackQuery()) {
//                    if (isButton(311)){
//                        sendMessageWithKeyboard("",310);
//                        waitingType = WaitingType.STUDENETS;
//                        return COMEBACK;
//                    } else if (isButton(312)) {
//                        sendMessageWithKeyboard("",310);
////                        waitingType = WaitingType.
//                    }
//                }
//            case OTCHET:
//                if (hasCallbackQuery()){
//                    if (isButton(302)){
//                        sendMessageWithKeyboard("",302);
//                        waitingType = WaitingType.FLUR;
//                        return COMEBACK;
//                    }
//                    return COMEBACK;
//                }
//            case FLUR:
//                if (hasCallbackQuery()){
//                    if (isButton(303)) {
//                        sendMessageWithKeyboard("", 303);
//                        waitingType = WaitingType.SPRAVKA;
//                        return COMEBACK;
//                    }
//                    return COMEBACK;
//                }
//            case SPRAVKA:
//                if (hasCallbackQuery()) {
//                    if (isButton(304)) {
//                        sendMessageWithKeyboard("",304);
//                        waitingType = WaitingType.VVK;
//                        return COMEBACK;
//                    }
//                    return COMEBACK;
//                }
//            case VVK:
//                if (hasCallbackQuery()){
//                    if (isButton(305)){
//                        sendMessageWithKeyboard("",305);
//                        waitingType = WaitingType.PASPORT;
//                        return COMEBACK;
//                    }
//                    return COMEBACK;
//                }
//            case PASPORT:
//                if (hasCallbackQuery()){
//                    if (isButton(306)){
//                        sendMessageWithKeyboard("",306);
//                        waitingType = WaitingType.NURSE;
//                        return COMEBACK;
//                    }
//                    return COMEBACK;
//                }
//        }
//
//        return EXIT;
//    }
//}
