package qbots.mektep.command.impl.psychologist;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.WaitingType;
import qbots.mektep.model.standart.Classroom;
import qbots.mektep.model.standart.Student;
import qbots.mektep.model.standart.StudentsTest.Test;
import qbots.mektep.service.ReportGenerator;
import qbots.mektep.util.ButtonsLeaf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class id601_TestReport extends Command {

    int delMess;

    List<Test> tests;
    Test currentTest;
    Classroom currentClassroom;
    ButtonsLeaf buttonsLeaf2;

    @Override
    public boolean execute() throws TelegramApiException {

        switch (waitingType){
            case START:
                tests = testRepo.findAllBy();
                sendClasees();
                return COMEBACK;
            case SET_CLASS:
                if (hasCallbackQuery()){
                    List<Classroom > classrooms = classroomRepo.findAllByNumber(Integer.parseInt(updateMessageText));

                    buttonsLeaf2 = new ButtonsLeaf(getNames3(classrooms),getIds3(classrooms) );
                    sendClasees2();
                }
                else {
                    wrongData();
                    sendClasees();
                }
                return COMEBACK;
            case SET_CLASS2:
                if (hasCallbackQuery()){
                    if(isButton(6)){
                        deleteUpdateMess();
                        sendClasees();
                    }
                    else if (classroomRepo.findById(getLong(updateMessageText))!= null){
                        currentClassroom = classroomRepo.findById(getLong(updateMessageText));
                        sendReport();
                    }
                    else {
                        wrongData();
                        sendClasees2();
                    }
                }
                else {
                    wrongData();
                    sendClasees2();
                }
                return COMEBACK;
        }
        return EXIT;
    }

    private void sendReport() throws TelegramApiException {
        ReportGenerator reportGenerator = new ReportGenerator();
        reportGenerator.message(chatId, bot, delMess, currentClassroom);
    }



    private void sendSuccess() throws TelegramApiException {
        deleteMessage(delMess);
        sendMessage(603);
    }



    private void sendClasees() throws TelegramApiException {
        deleteMessage( delMess);
        List<Classroom> classrooms = classroomRepo.findAll();
        Set<String> nums = new HashSet<>();

        for (Classroom classroom : classrooms){
            nums.add(String.valueOf(classroom.getNumber()));
        }
        List<String> mainList = new ArrayList<>(nums);
        ButtonsLeaf buttonsLeaf = new ButtonsLeaf(new ArrayList<String>(nums),new ArrayList<String>(nums) );
        delMess = sendMessageWithKeyboard("Выберите класс: " , buttonsLeaf.getListButtonWithDataList());
        waitingType = WaitingType.SET_CLASS;
    }


    private void sendClasees2() throws TelegramApiException {
        deleteMessage( delMess);

        delMess = sendMessageWithKeyboard("Выберите класс: " , buttonsLeaf2.getListButtonWithDataList());
        waitingType = WaitingType.SET_CLASS2;
    }

    private List<String> getNames3(List<Classroom> classrooms) {
        List<String> names = new ArrayList<>();
        for (Classroom classroom : classrooms){
            names.add(classroom.getName());
        }
        names.add(getButtonText(6));
        return names;
    }

    private List<String> getIds3(List<Classroom> classrooms) {
        List<String> names = new ArrayList<>();
        for (Classroom classroom : classrooms){
            names.add(String.valueOf(classroom.getId()));
        }
        names.add(getButtonText(6));
        return names;
    }
    private void sendTestList() throws TelegramApiException {
        deleteMessage(delMess);
        delMess = sendMessageWithKeyboard(414, new ButtonsLeaf(getNames(tests), getIds(tests)).getListButtonWithDataList());
        waitingType = WaitingType.CHOOSE_TEST;
    }


    private List<String> getIds(List<Test> tests) {
        List<String> ids = new ArrayList<>();
        for (Test test: tests){
            ids.add(String.valueOf(test.getId()));
        }
        return ids;
    }

    private List<String> getNames(List<Test> tests) {
        List<String> names = new ArrayList<>();
        for (Test test: tests){
            names.add(test.getName());
        }
        return names;
    }

    private void wrongData() throws TelegramApiException {
        sendMessage(4);
    }
}
