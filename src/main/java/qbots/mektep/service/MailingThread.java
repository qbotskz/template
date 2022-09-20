package qbots.mektep.service;


import lombok.Data;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import qbots.mektep.enums.FileType;
import qbots.mektep.model.standart.User;

import java.util.List;

@Data
public class MailingThread extends Thread {

    private DefaultAbsSender bot;
    private List<User> users;
    FileType fileType;
    InputFile inputFile;
    String text;
    int i = 0;

    public MailingThread(DefaultAbsSender bot) {
        this.bot = bot;
    }

//    public MailingThread(DefaultAbsSender bot, List<User> users, InputFile inputFile, FileType fileType, String text) {
//        this.bot = bot;
//        this.users = users;
//        this.inputFile = inputFile;
//        this.text = text;
//        this.fileType = fileType;
//    }

    @Override
    public void run() {

        for(User user : users){
            try {
                switch (fileType) {
                    case document:
                        SendDocument sendDocument = new SendDocument();
                        sendDocument.setDocument(inputFile);
                        sendDocument.setChatId(String.valueOf(user.getChatId()));
                        try {
                            bot.execute(sendDocument);
                        } catch (Exception ignored) {
                        }
                        break;
                    case photo:
                        SendPhoto sendPhoto = new SendPhoto();
                        sendPhoto.setPhoto(inputFile);
                        sendPhoto.setChatId(String.valueOf(user.getChatId()));
                        try {
                            bot.execute(sendPhoto);
                        } catch (Exception ignored) {
                        }
                        break;
                    case video:
                        SendVideo sendVideo = new SendVideo();
                        sendVideo.setVideo(inputFile);
                        sendVideo.setChatId(String.valueOf(user.getChatId()));
                        try {
                            bot.execute(sendVideo);
                        } catch (Exception ignored) {
                        }
                        break;
                    default:
                        break;
                }


                SendMessage sendMessage = new SendMessage();
                sendMessage.setParseMode("html");
                sendMessage.setText(text);
                sendMessage.setChatId(String.valueOf(user.getChatId()));


                bot.execute(sendMessage);
                sleepp();
            } catch (Exception e) {e.printStackTrace(); }
        }

    }

    private void sleepp() {
        i++;
        if (i >= 10){
            try {
                sleep(1500);
                i=0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}