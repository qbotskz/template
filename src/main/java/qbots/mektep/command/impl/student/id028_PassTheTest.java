package qbots.mektep.command.impl.student;

import com.itextpdf.text.DocumentException;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.FileType;
import qbots.mektep.enums.Role;
import qbots.mektep.enums.WaitingType;
import qbots.mektep.exceptions.ButtonNotFoundException;
import qbots.mektep.exceptions.CommandNotFoundException;
import qbots.mektep.exceptions.KeyboardNotFoundException;
import qbots.mektep.exceptions.MessageNotFoundException;
import qbots.mektep.model.standart.AppealToPsych;
import qbots.mektep.model.standart.Student;
import qbots.mektep.model.standart.StudentsTest.Question;
import qbots.mektep.model.standart.StudentsTest.StudentsAnswer;
import qbots.mektep.model.standart.StudentsTest.Test;
import qbots.mektep.model.standart.StudentsTest.Variant;
import qbots.mektep.model.standart.User;
import qbots.mektep.util.ButtonsLeaf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class id028_PassTheTest extends Command {

    private AppealToPsych appealFromStudent;
    User user;
    private int delMess;
    Student currentStudent;

    Test currentTest;
    Question currentQuestion;

    @Override
    public boolean execute() throws TelegramApiException, IOException, SQLException, FileNotFoundException, MessageNotFoundException, KeyboardNotFoundException, ButtonNotFoundException, CommandNotFoundException, DocumentException {
        if (!isRegistered()){
            sendChooseLanguage();
            return EXIT;
        }

        switch (waitingType){
            case START:

                deleteUpdateMess();
                user = usersRepo.findByChatId(chatId);
                currentStudent = user.getCurrentStudent();
                if (currentStudent != null){
                    sendTestsList();
                }
                else {
                    wrongData();
                }
                return COMEBACK;

            case CHOOSE_TEST:
                deleteUpdateMess();
                currentTest = testRepo.findById(getLong(updateMessageText));
                if (hasCallbackQuery() && currentTest!= null){
                    sendNextQuestion();
                }
                else {
                    wrongData();
                    sendTestsList();
                }
                return COMEBACK;
            case CHOOSE_VARIANT:
                deleteUpdateMess();
                Variant variant = variantRepo.findById(getLong(updateMessageText));
                if (hasCallbackQuery() && currentQuestion.getId() == variant.getQuestion().getId()){
                    StudentsAnswer studentsAnswer = new StudentsAnswer();
                    studentsAnswer.setStudent(currentStudent);
                    studentsAnswer.setQuestion(currentQuestion);
                    studentsAnswer.setVariant(variant);
                    studentsAnswerRepo.save(studentsAnswer);
                }
                else {
                    wrongData();
                }
                sendNextQuestion();
                return COMEBACK;
            
        }


        return EXIT;
    }

    private void sendNextQuestion() throws TelegramApiException {

        currentQuestion = questionRepo.getLastQuest(currentStudent.getId() , currentTest.getId());

        if (currentQuestion != null){
            ButtonsLeaf buttonsLeaf = new ButtonsLeaf(getNames2(currentQuestion.getVariants()), getIds2(currentQuestion.getVariants()));
            sendMessageWithKeyboard(currentQuestion.getQuestionText(), buttonsLeaf.getListButtonWithDataList());
            waitingType = WaitingType.CHOOSE_VARIANT;
        }
        else {
            sendMessage(415);
        }

    }

    private List<String> getIds2(List<Variant> variants) {
        List<String> ids = new ArrayList<>();
        for (Variant variant: variants){
            ids.add(String.valueOf(variant.getId()));
        }
        return ids;
    }

    private List<String> getNames2(List<Variant> variants) {
        List<String> names = new ArrayList<>();
        for (Variant variant: variants){
            names.add(variant.getText());
        }
        return names;
    }

    private void sendTestsList() throws TelegramApiException {
        deleteMessage(delMess);
        List<Test> tests = testRepo.findAllByClassroomsContains(currentStudent.getClassroom());
        //todo

        List<Test> notPassed = new ArrayList<>();
        for (Test test : tests){
            if (test.getQuestions().size()!= studentsAnswerRepo.countByStudentAndQuestionTest(currentStudent, test)){
                notPassed.add(test);
            }
        }


        if (notPassed.size() > 0) {
            ButtonsLeaf buttonsLeaf = new ButtonsLeaf(getNames(notPassed), getIds(notPassed));
            delMess = sendMessageWithKeyboard(414, buttonsLeaf.getListButtonWithDataList());
            waitingType = WaitingType.CHOOSE_TEST;
        }
        else {
            sendMessage(416);
        }
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

    private void sendFile() throws TelegramApiException {
        deleteMessage(delMess);
        delMess = sendMessage(403);
        waitingType = WaitingType.SET_PHOTO_VIDEO_FILE;
    }

    private void sendText() throws TelegramApiException {
        deleteMessage(delMess);
        delMess = sendMessage(402);
        waitingType = WaitingType.SET_TEXT;
    }

    private void sendSuccess() throws TelegramApiException {
        delete(delMess);
        sendMessage(404);
    }



    private void wrongData() throws TelegramApiException {
        sendMessage(4);
    }

    protected long getLong(String mess) {
        try {
            return Long.parseLong(mess);
        } catch (Exception e) {
            return -1;
        }
    }


}
