package qbots.mektep.command.impl;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.Role;
import qbots.mektep.enums.WaitingType;
import qbots.mektep.model.standart.Classroom;
import qbots.mektep.model.standart.RequestFromParent;
import qbots.mektep.model.standart.Student;
import qbots.mektep.model.standart.User;
import qbots.mektep.util.ButtonsLeaf;
import qbots.mektep.util.DateKeyboard;

import java.util.*;

@Slf4j
public class id202_AddChild extends Command {
    User user;
    Classroom currentClassroom;
    Student currentStudent;
    private int delMes;
    DateKeyboard dateKeyboard;



    List<Classroom> classrooms;


    @Override
    public boolean execute() throws TelegramApiException {
        if (!isRegistered()){
            sendChooseLanguage();
            return EXIT;
        }

        switch (waitingType){
            case START:
                deleteUpdateMess();
                user = usersRepo.findByChatId(chatId);

                sendClasees();
                return COMEBACK;
            case SET_CLASS:
                deleteUpdateMess();
                if (hasCallbackQuery()){
                    if (isButton(6)){
                        sendClasees();
                        return COMEBACK;
                    }
                    else {
                        classrooms = classroomRepo.findAllByNumber(Integer.parseInt(updateMessageText));
                        sendClasees2();
                    }
                }
                return COMEBACK;
            case SET_CLASS2:
                deleteUpdateMess();
                if (hasCallbackQuery()){
                    if(isButton(6)){
                        sendClasees();
                    }
                    else if (classroomRepo.findById(getLong(updateMessageText))!= null){
                        currentClassroom = classroomRepo.findById(getLong(updateMessageText));
                        sendStudents();
                    }
                }
                return COMEBACK;
            case CHOOSE_STUDENT:
                if (hasCallbackQuery()){
                    if (isButton(6)){
                        sendClasees2();
                    }
                    else if (studentRepo.findById(getLong(updateMessageText)) != null){
                        currentStudent = studentRepo.findById(getLong(updateMessageText));
                        sendSucces();
                        sendRequest();
                        return EXIT;
                    }
                }
                return COMEBACK;

        }

        return COMEBACK;
    }

    private void sendSucces() throws TelegramApiException {
        sendMessage("Ваша заявка была отправлена! Вам откроется доступ как только классный рук. подтвердит");
    }

    private void sendRequest() throws TelegramApiException {
        user = usersRepo.findByChatId(chatId);
        RequestFromParent requestFromParent = new RequestFromParent();
        requestFromParent.setParent(user);
        requestFromParent.setStudent(currentStudent);
        requestFromParent = requestFromParentRepo.save(requestFromParent);

        ButtonsLeaf buttonsLeaf = new ButtonsLeaf(Arrays.asList(getButtonText(503), getButtonText(504)), Arrays.asList(getButtonText(503)+";"+requestFromParent.getId(),getButtonText(504)+";"+requestFromParent.getId()));

        String str = "Запрос от родителя:"+user.getFullName() + " " + user.getPhone() + next+
                "Имя ученика: " + currentStudent.getFullName() + next;

        sendMessageWithKeyboard(str, buttonsLeaf.getListButtonWithDataList(), currentStudent.getClassroom().getTeacher().getUser().getChatId());
    }

    private void sendStudents() throws TelegramApiException {
        deleteMessage(delMes);
//        List<Student> students = getWithountParents(currentClassroom.getStudents());
        ButtonsLeaf buttonsLeaf = new ButtonsLeaf(getNames2(getWithountParents(currentClassroom.getStudents())), getIds2(getWithountParents(currentClassroom.getStudents())));
        delMes = sendMessageWithKeyboard(129, buttonsLeaf.getListButtonWithDataList());
        waitingType = WaitingType.CHOOSE_STUDENT;
    }

    private List<Student> getWithountParents(List<Student> students) {
        List<Student> studentList = new ArrayList<>();
        for (Student student : students){
            if (student.getParent() == null){
                studentList.add(student);
            }
        }
        return studentList;
    }

    private void sendClasees2() throws TelegramApiException {
        deleteMessage( delMes);

        ButtonsLeaf buttonsLeaf = new ButtonsLeaf(getNames(classrooms),getIds(classrooms) );
        delMes = sendMessageWithKeyboard("Выберите класс: " , buttonsLeaf.getListButtonWithDataList());
        waitingType = WaitingType.SET_CLASS2;
    }

    private List<String> getNames(List<Classroom> classrooms) {
        List<String> names = new ArrayList<>();
        for (Classroom classroom : classrooms){
            names.add(classroom.getName());
        }
        names.add(getButtonText(6));
        return names;
    }

    private List<String> getIds(List<Classroom> classrooms) {
        List<String> names = new ArrayList<>();
        for (Classroom classroom : classrooms){
            names.add(String.valueOf(classroom.getId()));
        }
        names.add(getButtonText(6));
        return names;
    }



    private List<String> getIds2(List<Student> students) {
        List<String> names = new ArrayList<>();
        for (Student student : students){
            names.add(String.valueOf(student.getId()));
        }
        names.add(getButtonText(6));
        return names;
    }

    private List<String> getNames2(List<Student> students) {
        List<String> names = new ArrayList<>();
        for (Student student : students){
            names.add(student.getFullName());
        }
        names.add(getButtonText(6));
        return names;
    }
    private void sendClasees() throws TelegramApiException {
        deleteMessage( delMes);
        List<Classroom > classrooms = classroomRepo.findAll();
        Set<String> nums = new HashSet<>();


        for (Classroom classroom : classrooms){
            nums.add(String.valueOf(classroom.getNumber()));
        }
        List<String> mainList = new ArrayList<>(nums);
        ButtonsLeaf buttonsLeaf = new ButtonsLeaf(new ArrayList<String>(nums),new ArrayList<String>(nums) );
        delMes = sendMessageWithKeyboard("Выберите класс: " , buttonsLeaf.getListButtonWithDataList());
        waitingType = WaitingType.SET_CLASS;
    }



//    private List<String> getIds(List<Student> students) {
//        List<String> names = new ArrayList<>();
//        for (Student student : students){
//            names.add(String.valueOf(student.getId()));
//        }
//        return names;
//    }
//
//    private List<String> getNames(List<Student> students) {
//        List<String> names = new ArrayList<>();
//        for (Student student : students){
//            names.add(student.getFullName());
//        }
//        return names;
//    }


    protected boolean isLong(String mess) {
        try {
            Long.parseLong(mess);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private void wrongData() throws TelegramApiException {
        sendMessage(4);
    }
}
