package qbots.project.command;

import com.itextpdf.text.DocumentException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.project.enums.FileType;
import qbots.project.enums.Language;
import qbots.project.enums.Role;
import qbots.project.enums.WaitingType;
import qbots.project.exceptions.ButtonNotFoundException;
import qbots.project.exceptions.CommandNotFoundException;
import qbots.project.exceptions.KeyboardNotFoundException;
import qbots.project.exceptions.MessageNotFoundException;
import qbots.project.model.standart.Button;
import qbots.project.repository.*;
import qbots.project.service.KeyboardMarkUpService;
import qbots.project.service.LanguageService;
import qbots.project.util.BotUtil;
import qbots.project.util.SetDeleteMessages;
import qbots.project.util.UpdateUtil;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@NoArgsConstructor
public abstract class Command {


    @Getter
    @Setter
    protected long id;
    protected Long chatId;
    protected Update update;
    @Getter
    @Setter
    protected long messageId;
    protected String markChange;
    protected int updateMessageId;
    protected DefaultAbsSender bot;
    protected int lastSentMessageID;
    protected static BotUtil botUtils;
    protected String updateMessageText;
    protected String updateMessagePhoto;
    protected String updateMessagePhone;
    protected WaitingType waitingType = WaitingType.START;
    protected String editableTextOfMessage;
    protected final static String linkEdit = "/linkId";
    protected static final String next = "\n";
    protected static final String space = " ";
    protected final static boolean EXIT = true;
    protected final static boolean COMEBACK = false;
    protected Message updateMessage;

    protected KeyboardMarkUpService keyboardMarkUpService = new KeyboardMarkUpService();
    protected UsersRepo usersRepo = TelegramBotRepositoryProvider.getUsersRepo();
    protected MessageRepo messageRepo = TelegramBotRepositoryProvider.getMessageRepo();
    protected ButtonRepo buttonRepo = TelegramBotRepositoryProvider.getButtonRepo();
    protected KeyboardRepo keyboardRepo = TelegramBotRepositoryProvider.getKeyboardRepo();
    protected PropertiesRepo propertiesRepo = TelegramBotRepositoryProvider.getPropertiesRepo();

    //------------------------------------------------------------------------


    public abstract boolean execute() throws TelegramApiException, IOException, SQLException, FileNotFoundException, MessageNotFoundException, KeyboardNotFoundException, ButtonNotFoundException, CommandNotFoundException, DocumentException;

    public static String formatPhone(String phoneNumber) {

        if (phoneNumber != null && !phoneNumber.equals("")) {
            if (phoneNumber.startsWith("8")) {
                phoneNumber = phoneNumber.replaceFirst("8", "+7");
            }
            if (phoneNumber.startsWith("7")) {
                phoneNumber = phoneNumber.replaceFirst("7", "+7");
            }
        }
        return phoneNumber;
    }

    protected boolean isIIN(String updateMessageText) {
        try {
            Long.parseLong(updateMessageText);
            return updateMessageText.length() == 12;
        }catch (Exception e){
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
    protected int getInt(String mess) {
        try {
            return Integer.parseInt(mess);
        } catch (Exception e) {
            return -1;
        }
    }
    protected Language getLanguage() {
        if (chatId == 0) return Language.ru;
        return LanguageService.getLanguage(chatId);
    }

    protected Language getLanguage(long chatId) {
        if (chatId == 0) return Language.ru;
        return LanguageService.getLanguage(chatId);
    }

    protected String getButtonText(int btnId) {
        Button button = buttonRepo.findByIdAndLangId(btnId, getLangId());
        if (button!= null)
            return button.getName();
        return null;
    }



    protected int getLangId() {
        return getLanguage().getId();
    }

    public void editMessageWithKeyboard(String text, long chatId, int messageId, InlineKeyboardMarkup replyKeyboard) throws TelegramApiException {
        EditMessageText new_message = new EditMessageText();
        new_message.setChatId(String.valueOf(chatId));
        new_message.setMessageId(messageId);
        new_message.setText(text);
        new_message.setReplyMarkup(replyKeyboard);
        new_message.setParseMode("html");
        try {
            bot.execute(new_message);
        } catch (TelegramApiException e) {
            if (e.toString().contains("Bad Request: can't parse entities")) {
                new_message.setParseMode(null);
                bot.execute(new_message);
            } else e.printStackTrace();
        }

    }

    public void editMessage(String text, long chatId, int messageId) throws TelegramApiException {
        EditMessageText new_message = new EditMessageText();
        new_message.setChatId(String.valueOf(chatId));
        new_message.setMessageId(messageId);
        new_message.setText(text);
        try {
            bot.execute(new_message);
        } catch (TelegramApiException e) {
            if (e.toString().contains("Bad Request: can't parse entities")) {
                new_message.setParseMode(null);
                bot.execute(new_message);
            }
            e.printStackTrace();
        }
    }

    public int sendMedia(String fileId, FileType fileType , long chatId) throws TelegramApiException {
        if (fileId != null && fileType != null) {
            switch (fileType) {
                case photo:
                    return sendPhoto(fileId, chatId);
                case video:
                    return sendVideo(fileId, chatId);
                case document:
                    return sendDocument(fileId, chatId);
                default:
                    break;
            }
        }

        return 0;
    }

    public int  sendMedia(String fileId, FileType fileType ) throws TelegramApiException {
        return sendMedia(fileId, fileType ,chatId);
    }

    public int sendPhoto(String photo, long chatId) throws TelegramApiException {
        SendPhoto sendPhoto             = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(chatId));
        sendPhoto.setPhoto(new InputFile(photo));

        try {
            return bot.execute(sendPhoto).getMessageId();
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.debug("Can't send photo", e);
        }
        return 0;
    }
    public int sendVideo(String video, long chatId) throws TelegramApiException {

        SendVideo sendVideo = new SendVideo();
        sendVideo.setVideo(new InputFile(video));
        sendVideo.setChatId(String.valueOf(chatId));


        try {
            return bot.execute(sendVideo).getMessageId();
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.debug("Can't send video", e);
        }
        return 0;
    }

    public int sendDocument(String fileId, long chatId) throws TelegramApiException {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(String.valueOf(chatId));
        sendDocument.setDocument(new InputFile(fileId));



        try {
            return bot.execute(sendDocument).getMessageId();
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.debug("Can't send document", e);
        }
        return 0;
    }

    public int sendDocument(String fileId,String mess, long chatId) throws TelegramApiException {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(String.valueOf(chatId));
        sendDocument.setDocument(new InputFile(fileId));
        sendDocument.setCaption(mess);


        try {
            return bot.execute(sendDocument).getMessageId();
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.debug("Can't send document", e);
        }
        return 0;
    }

    public void editMessageWithKeyboard(String text, InlineKeyboardMarkup inlineKeyboardMarkup, int messageId) throws TelegramApiException {
        EditMessageText new_message = new EditMessageText();
        new_message.setChatId(String.valueOf(chatId));
        new_message.setMessageId(messageId);
        new_message.setText(text);
        new_message.setReplyMarkup(inlineKeyboardMarkup);
        try {
            bot.execute(new_message);
        } catch (TelegramApiException e) {
            if (e.toString().contains("Bad Request: can't parse entities")) {
                new_message.setParseMode(null);
                bot.execute(new_message);
            }
            e.printStackTrace();
        }
    }

    protected int sendMessage(long messageId) throws TelegramApiException {
        return sendMessage(messageId, chatId);
    }

    protected int sendMessage(long messageId, long chatId) throws TelegramApiException {
        return sendMessage(messageId, chatId, null);
    }

    protected int sendMessage(long messageId, long chatId, Contact contact) throws TelegramApiException {
        return sendMessage(messageId, chatId, contact, null);
    }

    protected int sendMessage(long messageId, long chatId, Contact contact, String photo) throws TelegramApiException {
//        lastSentMessageID =
        return       botUtils.sendMessage(messageId, chatId, contact, photo);
//        return lastSentMessageID;
    }

    protected int sendMessage(String text) throws TelegramApiException {
        return sendMessage(text, chatId);
    }

    protected int sendMessage(String text, long chatId) throws TelegramApiException {
        return sendMessage(text, chatId, null);
    }

    protected int sendMessage(String text, long chatId, Contact contact) throws TelegramApiException {
        lastSentMessageID = botUtils.sendMessage(text, chatId);
        if (contact != null) {
            botUtils.sendContact(chatId, contact);
        }
        return lastSentMessageID;
    }

    protected void deleteMessage() {
        deleteMessage(chatId, lastSentMessageID);
    }

    protected void deleteMessage(int messageId) {
        if (messageId != 0)
            deleteMessage(chatId, messageId);
    }

    protected void deleteMessage(long chatId, int messageId) {
        if (messageId != 0)
            botUtils.deleteMessage(chatId, messageId);
    }

    protected String getText(int messageIdFromBD) {
        return messageRepo.findByIdAndLanguageId(messageIdFromBD, getLanguage().getId()).getName();
    }

    protected Optional<String> getTextOptional(int messageIdFromDb) {
        return messageRepo.getName(messageIdFromDb, getLanguage().getId());
    }

    public void clear() {
        update = null;
        bot = null;
    }

    protected boolean isButton(int buttonId) {
        Button button = buttonRepo.findByIdAndLangId(buttonId, getLanguage().getId());
        return updateMessageText.equals(button.getName());
    }

    public boolean isInitNormal(Update update, DefaultAbsSender bot) {
        if (botUtils == null) botUtils = new BotUtil(bot);
        this.update = update;
        this.bot = bot;
        chatId = UpdateUtil.getChatId(update);
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            updateMessage = callbackQuery.getMessage();
            updateMessageText = callbackQuery.getData();
            updateMessageId = updateMessage.getMessageId();
            editableTextOfMessage = callbackQuery.getMessage().getText();
        } else if (update.hasMessage()) {
            updateMessage = update.getMessage();
            updateMessageId = updateMessage.getMessageId();
            if (updateMessage.hasText()) updateMessageText = updateMessage.getText();
            if (updateMessage.hasPhoto()) {
                int size = update.getMessage().getPhoto().size();
                updateMessagePhoto = update.getMessage().getPhoto().get(size - 1).getFileId();
            } else {
                updateMessagePhoto = null;
            }
        }
        if (hasContact()) updateMessagePhone = update.getMessage().getContact().getPhoneNumber();
//        if (markChange == null) markChange      = getText(Const.EDIT_BUTTON_ICON);
        return COMEBACK;
    }

    protected boolean isUser(long chatId) {
        int count = usersRepo.countUserByChatId(chatId);
        if (count > 0) return EXIT;
        return COMEBACK;
    }

//    private String getUpdateMessageText(){
//        return "0sdcds";
//    }

//
//    protected void sendMessageWithAddition() throws TelegramApiException {
//        deleteMessage(updateMessageId);
//        int languageId = getLanguage().getId();
//        qbots.mektep.model.standart.Message message = messageRepo.findByIdAndLanguageId((int) messageId, languageId);
//        try {
//            if (message.getFile() != null || message.getFileType() != null) {
//                switch (message.getFileType()) {
//                    case "photo":
//                        SendPhoto sendPhoto = new SendPhoto();
//                        sendPhoto.setPhoto(new InputFile(message.getFile()));
//                        sendPhoto.setChatId(String.valueOf(chatId));
//                        bot.execute(sendPhoto);
//                        break;
//                    case "audio":
//                        SendAudio sendAudio = new SendAudio();
//                        sendAudio.setAudio(new InputFile(message.getFile()));
//                        sendAudio.setChatId(String.valueOf(chatId));
//                        bot.execute(sendAudio);
//                        break;
//                    case "video":
//                        SendVideo sendVideo = new SendVideo();
//                        sendVideo.setVideo(new InputFile(message.getFile()));
//                        sendVideo.setChatId(String.valueOf(chatId));
//                        bot.execute(sendVideo);
//                        break;
//                    case "document":
//                        SendDocument sendDocument = new SendDocument();
//                        sendDocument.setDocument(new InputFile(message.getFile()));
//                        sendDocument.setChatId(String.valueOf(chatId));
//                        bot.execute(sendDocument);
//                        break;
//
//                }
//            }
//            sendMessage(messageId, chatId, null, message.getPhoto());
//        } catch (TelegramApiException e) {
//            log.error("Exception by send file for message " + messageId, e);
//        }
//    }

    protected int sendAdminPanel() throws TelegramApiException {
        return sendMessageWithKeyboard(1001,7);
    }






    protected int sendPatientPanel() throws TelegramApiException {
        return sendMessageWithKeyboard(835,44);
    }


    protected int sendNursePanel() throws TelegramApiException {
        return sendMessageWithKeyboard(835,44);
    }

    protected int sendStartMenu() throws TelegramApiException {
        return sendMessageWithKeyboard(1,1);
    }
    protected int sendChooseLanguage() throws TelegramApiException {
        return sendMessageWithKeyboard(5,2);
    }

    protected boolean isAdmin() {
        qbots.project.model.standart.User user = usersRepo.findByChatId(chatId);
        return  (user != null && user.getRoles().contains(Role.ROLE_ADMIN));
    }

//    protected boolean isEmployee() {
//        int count = employeeRepo.countByChatId(chatId);
//        if (count > 0) return EXIT;
//        return COMEBACK;
//    }

    protected boolean isAdmin(Long chat) {
        qbots.project.model.standart.User user = usersRepo.findByChatId(chatId);
        return  (user != null && user.getRoles().contains(Role.ROLE_ADMIN));
    }

//    protected boolean isEmployee(Long chat) {
//        int count = employeeRepo.countByChatId(chat);
//        if (count > 0) return EXIT;
//        return COMEBACK;
//    }

    protected boolean isButtonExist(String name) {

        return buttonRepo.countByNameAndLangId(name, getLangId()) > 0;
    }



    protected String getLinkForUser(long chatId, String userName) {
        return String.format("<a href = \"tg://user?id=%s\">%s</a>", chatId, userName);
    }

    protected int toDeleteMessage(int messageDeleteId) {
        SetDeleteMessages.addKeyboard(chatId, messageDeleteId);
        return messageDeleteId;
    }

    protected int toDeleteKeyboard(int messageDeleteId) {
        SetDeleteMessages.addKeyboard(chatId, messageDeleteId);
        return messageDeleteId;
    }

    protected int sendMessageWithKeyboard(int messageId, ReplyKeyboard keyboard) throws TelegramApiException {
        return sendMessageWithKeyboard(getText(messageId), keyboard);
    }

    protected int sendMessageWithKeyboard(int messageId, int keyboardId) throws TelegramApiException {
        return sendMessageWithKeyboard(getText(messageId), keyboardMarkUpService.select(keyboardId));
    }

    protected int sendMessageWithKeyboard(String text, int keyboardId) throws TelegramApiException {
        return sendMessageWithKeyboard(text, keyboardMarkUpService.select(keyboardId));
    }

    protected int sendMessageWithKeyboard(String text, ReplyKeyboard keyboard) throws TelegramApiException {
        lastSentMessageID = sendMessageWithKeyboard(text, keyboard, chatId);
        return lastSentMessageID;
    }

    protected int sendMessageWithKeyboard(String text, ReplyKeyboard keyboard, long chatId) throws TelegramApiException {
        return botUtils.sendMessageWithKeyboard(text, keyboard, chatId);
    }
    protected int sendMessageWithKeyboard(int text, int keyboardId, long chatId) throws TelegramApiException {
        return botUtils.sendMessageWithKeyboard(getText(text),  keyboardMarkUpService.select(keyboardId), chatId);
    }

    protected boolean isExist(String buttonName) {
        return buttonRepo.countByNameAndLangId(buttonName, getLanguage().getId()) > 0;
    }

    protected void delete(int updateMessageId, int deleteMesId) {
        deleteMessage(updateMessageId);
        deleteMessage(deleteMesId);
        deleteMessage(lastSentMessageID);
    }

    protected void delete(int updateMessageId) {
        deleteMessage(updateMessageId);
        deleteMessage(lastSentMessageID);
    }


    protected String uploadFile(String fileId) {
        Objects.requireNonNull(fileId);
        GetFile getFile = new GetFile();
        getFile.setFileId(fileId);
        try {
            File file = bot.execute(getFile);
            return file.getFilePath();
        } catch (TelegramApiException e) {
            throw new IllegalStateException(e);
        }
    }

    protected void sendStudentMenu() throws TelegramApiException {
        sendMessageWithKeyboard(400, 15);
    }

    protected void sendPsychologistMenu() throws TelegramApiException {
        sendMessageWithKeyboard(600, 17);
    }


    protected boolean hasContact() {
        return update.hasMessage() && update.getMessage().getContact() != null;
    }

    protected boolean isRegistered() {
        qbots.project.model.standart.User user = usersRepo.findByChatId(chatId);
       return  (user != null && user.getPhone()!= null);
//        return usersRepo.countUserByChatId(chatId) > 0;
    }

    protected void deleteUpdateMess() {
        deleteMessage(updateMessageId);
    }

    protected boolean hasCallbackQuery() {
        return update.hasCallbackQuery();
    }

    protected boolean hasPhoto() {
        return update.hasMessage() && update.getMessage().hasPhoto();
    }

    protected boolean hasDocument() {
        return update.hasMessage() && update.getMessage().hasDocument();
    }

    protected boolean hasAudio() {
        return update.hasMessage() && update.getMessage().getAudio() != null;
    }

    protected boolean hasVideo() {
        return update.hasMessage() && update.getMessage().getVideo() != null;
    }

    protected boolean hasMessageText() {
        return update.hasMessage() && update.getMessage().hasText();
    }
}
