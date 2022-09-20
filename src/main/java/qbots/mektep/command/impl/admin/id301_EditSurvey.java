package qbots.mektep.command.impl.admin;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.Role;
import qbots.mektep.enums.WaitingType;
import qbots.mektep.model.standart.Classroom;
import qbots.mektep.model.standart.StudentsTest.Question;
import qbots.mektep.model.standart.StudentsTest.Test;
import qbots.mektep.model.standart.StudentsTest.Variant;
import qbots.mektep.model.standart.Teacher;
import qbots.mektep.model.standart.User;
import qbots.mektep.util.ButtonsLeaf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class id301_EditSurvey extends Command {

    int delMess;
    Test currentTest;
    Question currentQuestion;
    Variant variantNew;

    @Override
    public boolean execute() throws TelegramApiException {
        if (!isRegistered() || !isAdmin()){
            sendChooseLanguage();
            return EXIT;
        }


        switch (waitingType){
            case START:
                deleteUpdateMess();
                sendTestList();
                return COMEBACK;
            case CHOOSE_TEST:
                deleteUpdateMess();
                currentTest = testRepo.findById(getLong(updateMessageText));
                if (hasCallbackQuery() && currentTest!= null){
                    sendQuestions();
                }
                else if (hasCallbackQuery() && isButton(9)){
                    sendTestName();
                }
                else {
                    wrongData();
                    sendTestList();
                }
                return COMEBACK;
            case SET_NAME:
                deleteUpdateMess();
                if (hasMessageText()){
                    Test test = new Test();
                    test.setName(updateMessageText);
                    testRepo.save(test);

                    sendMessage(15);
                    sendTestList();
                }
                else {
                    wrongData();
                    sendTestName();
                }
                return COMEBACK;
            case CHOOSE_QUESTIONS:
                deleteUpdateMess();
                if (hasCallbackQuery()) {
                    currentQuestion = questionRepo.findById(getLong(updateMessageText));
                    if (currentQuestion != null){
                        sendVariants();
                    }
                    else if (isButton(10)){
                        sendNameQuestion();
                    }
                    else {
                        wrongData();
                        sendQuestions();
                    }
                }
                else {
                    wrongData();
                    sendQuestions();
                }

                return COMEBACK;
            case SET_NAME_QUESTION:
                deleteUpdateMess();
                if (hasMessageText()){
                    Question question = new Question();
                    question.setQuestionText(updateMessageText);
                    question.setTest(currentTest);
                    questionRepo.save(question);
                    sendMessage(17);
                    currentTest = testRepo.findById(currentTest.getId());
                    sendQuestions();
                }
                return COMEBACK;
            case CHOOSE_VARIANT:
                if (hasCallbackQuery() && isButton(11)){
                    deleteUpdateMess();
                    sendNameVariant();
                }
                else if (hasCallbackQuery() && isButton(6)){
                    deleteUpdateMess();
                    sendQuestions();
                }
                else {
                    wrongData();
                    sendVariants();
                }
                return COMEBACK;
            case SET_NAME_VARIANT:
                deleteUpdateMess();
                if (hasMessageText()){
                    variantNew = new Variant();
                    variantNew.setText(updateMessageText);
                    variantNew.setQuestion(currentQuestion);
                    sendPoints();
                }
                else {
                    wrongData();
                    sendNameVariant();
                }
                return COMEBACK;
            case SET_POINT_VARIANT:
                deleteUpdateMess();
                if (hasMessageText() && getInt(updateMessageText) != -1){
                    variantNew.setPoint(getInt(updateMessageText));
                    variantRepo.save(variantNew);
                    sendMessage(20);
                    currentQuestion = questionRepo.findById(currentQuestion.getId());
                    sendVariants();
                }
                else {
                    wrongData();
                    sendPoints();
                }
                return COMEBACK;

        }

        return EXIT;
    }

    private void sendPoints() throws TelegramApiException {
        deleteMessage(delMess);
        delMess = sendMessage(19);
        waitingType=WaitingType.SET_POINT_VARIANT;
    }

    private void sendNameVariant() throws TelegramApiException {
        deleteMessage(delMess);
        delMess = sendMessage(18);
        waitingType=WaitingType.SET_NAME_VARIANT;
    }

    private void sendNameQuestion() throws TelegramApiException {
        deleteMessage(delMess);
        delMess = sendMessage(16);
        waitingType=WaitingType.SET_NAME_QUESTION;
    }

    private void sendTestName() throws TelegramApiException {
        delMess = sendMessage(14);
        waitingType = WaitingType.SET_NAME;
    }

    private void sendVariants() throws TelegramApiException {
        deleteMessage(delMess);
        delMess = sendMessageWithKeyboard(13, new ButtonsLeaf(getNames3(currentQuestion.getVariants()), getIds3(currentQuestion.getVariants())).getListButtonWithDataList());
        waitingType = WaitingType.CHOOSE_VARIANT;
    }

    private List<String> getIds3(List<Variant> variants) {
        List<String> ids = new ArrayList<>();
        for (Variant variant: variants ){
            ids.add(String.valueOf(variant.getId()));
        }
        ids.add(getButtonText(11));
        ids.add(getButtonText(6));
        return ids;
    }

    private List<String> getNames3(List<Variant> variants) {
        List<String> names = new ArrayList<>();
        for (Variant variant: variants){
            names.add(variant.getText());
        }
        names.add(getButtonText(11));
        names.add(getButtonText(6));
        return names;
    }

    private void sendQuestions() throws TelegramApiException {
        deleteMessage(delMess);
        delMess = sendMessageWithKeyboard(12, new ButtonsLeaf(getNames2(currentTest.getQuestions()), getIds2(currentTest.getQuestions())).getListButtonWithDataList());
        waitingType = WaitingType.CHOOSE_QUESTIONS;
    }

    private List<String> getIds2(List<Question> questions) {
        List<String> ids = new ArrayList<>();
        for (Question question: questions){
            ids.add(String.valueOf(question.getId()));
        }
        ids.add(getButtonText(10) );
//        ids.add(getButtonText(9));
        return ids;
    }

    private List<String> getNames2(List<Question> questions) {
        List<String> names = new ArrayList<>();
        for (Question question: questions){
            names.add(question.getQuestionText());
        }
        names.add(getButtonText(10));
        return names;
    }

    private void wrongData() throws TelegramApiException {
        sendMessage(4);
    }



    private void sendTestList() throws TelegramApiException {
        List<Test> tests = testRepo.findAllBy();
        deleteMessage(delMess);
        delMess = sendMessageWithKeyboard(414, new ButtonsLeaf(getNames(tests), getIds(tests)).getListButtonWithDataList());
        waitingType = WaitingType.CHOOSE_TEST;
    }

    private List<String> getIds(List<Test> tests) {
        List<String> ids = new ArrayList<>();
        for (Test test: tests){
            ids.add(String.valueOf(test.getId()));
        }
        ids.add(getButtonText(9));
        return ids;
    }

    private List<String> getNames(List<Test> tests) {
        List<String> names = new ArrayList<>();
        for (Test test: tests){
            names.add(test.getName());
        }
        names.add(getButtonText(9));
        return names;
    }

    private List<Classroom> getClassrooms(String stringValue, Teacher teacher) {
        List<Classroom> classrooms = new ArrayList<>();
        for (String classroom : stringValue.split(",")){
            try {
                int number = getInt(classroom.split(" ")[0]);
                String letter = classroom.split(" ")[1];

                if (number >0 && number < 12 && letter.length() == 1) {
                    Classroom classroom1 = classroomRepo.findByNumberAndLetter(number, letter);
                    if (classroom1 == null) {
                        classroom1 = new Classroom();
                        classroom1.setNumber(number);
                        classroom1.setLetter(letter);

                    }
                    classroom1.setTeacher(teacher);
                    classroom1 = classroomRepo.save(classroom1);
                    classrooms.add(classroom1);
                }

            }catch (Exception ignored){}

        }

        return classrooms;
    }

    private String getStringValue(Row row, int i) {
        try {
            return row.getCell(i).getStringCellValue();
        }catch (Exception e){
            return getNumericValue(row , i);
        }
    }

    private String getNumericValue(Row row, int i) {
        Double phoneDouble;
        try {
            phoneDouble = row.getCell(i).getNumericCellValue();
            return  String.valueOf(phoneDouble.longValue());
        } catch (Exception e) {
            return "";
        }
    }
}
