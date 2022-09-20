package qbots.mektep.command.impl;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.WaitingType;
import qbots.mektep.util.Const;


import java.sql.SQLException;

public class id004_FileOrPhoto extends Command {
    @Override
    public boolean execute() throws TelegramApiException, SQLException {
        if (!isAdmin()) {
            sendMessage(Const.NO_ACCESS);
            return EXIT;
        }
        switch (waitingType){
            case START:
                sendMessage("Photo");
                waitingType = WaitingType.PHOTO;
                return COMEBACK;
            case PHOTO:
                if (update.getMessage().hasDocument())  sendMessage(update.getMessage().getDocument().getFileId());
                if (update.getMessage().hasPhoto()) sendMessage(updateMessagePhoto);
                if (updateMessage.hasAudio()) sendMessage(updateMessage.getAudio().getFileId());
                if (updateMessage.hasVideo()) sendMessage(updateMessage.getVideo().getFileId());
                return EXIT;
        }
        return EXIT;
    }
}
