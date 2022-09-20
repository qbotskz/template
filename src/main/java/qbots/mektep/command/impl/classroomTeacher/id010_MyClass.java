package qbots.mektep.command.impl.classroomTeacher;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.FileType;
import qbots.mektep.enums.WaitingType;
import qbots.mektep.model.standart.Classroom;
import qbots.mektep.model.standart.Student;
import qbots.mektep.model.standart.Teacher;
import qbots.mektep.model.standart.User;
import qbots.mektep.service.MailingThread;
import qbots.mektep.util.ButtonsLeaf;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class id010_MyClass extends Command {
    User user;

    Teacher teacher;
    Classroom currentClassroom;

    private int delMess;


    @Override
    public boolean execute() throws TelegramApiException {
        if (!isRegistered()){
            sendChooseLanguage();
            return EXIT;
        }

        switch (waitingType){
            case START:
                deleteUpdateMess();
                if (isButton(134)){
                    sendClassTeacherPanel();
                    return EXIT;
                }
                teacher = teacherRepo.findByUserChatId(chatId);
                if (teacher != null && teacher.getMyClasses().size()!=0){
                    if (teacher.getMyClasses().size() == 1){
                        teacher.setCurrentClassroom(teacher.getMyClasses().get(0));
                        teacherRepo.save(teacher);
                        currentClassroom = teacher.getMyClasses().get(0);

                        sendMessageWithKeyboard(126, 12);
                    }
                    else {
                        sendMyClasses();
                    }
                }
                else {
                    sendMessage("У вас нету классов");
                }
                return COMEBACK;
            case CHOOSE_CLASS:
                deleteUpdateMess();
                if (hasCallbackQuery()){
                    currentClassroom = classroomRepo.findById(getLong(updateMessageText));
                    if (currentClassroom != null){
                        teacher.setCurrentClassroom(currentClassroom);
                        teacherRepo.save(teacher);
                        sendMessageWithKeyboard(126, 12);
                    }
                    else {
                        wrongData();
                        sendMyClasses();
                    }
                }
                else {
                    wrongData();
                    sendMyClasses();
                }
                return COMEBACK;
        }

        return COMEBACK;
    }

    private void sendMyClasses() throws TelegramApiException {
        deleteMessage(delMess);
        ButtonsLeaf buttonsLeaf = new ButtonsLeaf(getNames(teacher.getMyClasses()), getIds(teacher.getMyClasses()));
        delMess = sendMessageWithKeyboard(121, buttonsLeaf.getListButtonWithDataList());
        waitingType = WaitingType.CHOOSE_CLASS;
    }



    private List<String> getIds(List<Classroom> classrooms) {
        List<String> names = new ArrayList<>();
        for (Classroom classroom : classrooms){
            names.add(String.valueOf(classroom.getId()));
        }
        names.add(getButtonText(6));
        return names;
    }

    private List<String> getNames(List<Classroom> classrooms) {
        List<String> names = new ArrayList<>();
        for (Classroom classroom : classrooms){
            names.add(classroom.getName());
        }
        names.add(getButtonText(6));
        return names;
    }

    private void sendWho() throws TelegramApiException {
        deleteMessage(delMess);
        delMess = sendMessageWithKeyboard(120, 9);
        waitingType = WaitingType.SET_WHO;
    }

    protected boolean isLong(String mess) {
        try {
            Long.parseLong(mess);
            return true;
        } catch (Exception e) {
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

    private void wrongData() throws TelegramApiException {
        sendMessage(4);
    }
}
