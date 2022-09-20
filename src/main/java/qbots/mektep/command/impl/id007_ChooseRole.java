package qbots.mektep.command.impl;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.Role;
import qbots.mektep.enums.WaitingType;

import qbots.mektep.model.standart.Student;
import qbots.mektep.model.standart.Teacher;
import qbots.mektep.model.standart.User;
import qbots.mektep.util.ButtonsLeaf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class id007_ChooseRole extends Command {
    User user;

    private int messId;

    @Override
    public boolean execute() throws TelegramApiException {
        if (!isRegistered()){
            sendChooseLanguage();
            return EXIT;
        }

        switch (waitingType){
            case START:
                sendListRoles();
                return COMEBACK;
            case CHOOSE_ROLE:
                deleteUpdateMess();
                if (hasCallbackQuery()){
                    sendRolePanel();
                }
                return EXIT;
        }

        return COMEBACK;
    }

    private void sendRolePanel() throws TelegramApiException {
        if (updateMessageText.equals(Role.ROLE_ADMIN.name())){
            sendAdminPanel();
        }
        else if (updateMessageText.equals(Role.ROLE_CLASSROOM_TEACHER.name())){
            sendClassTeacherPanel();
        }

        else if (updateMessageText.equals(Role.ROLE_PARENT.name())){
            sendStartMenu();
        }

        else if (updateMessageText.equals(Role.ROLE_PSYCHOLOGIST.name())){
            sendPsychologistMenu();
        }

        else if(updateMessageText.split(";").length==2){
            Student student = studentRepo.findById(getLong(updateMessageText.split(";")[1]));
            user.setCurrentStudent(student);
            usersRepo.save(user);
            sendStudentMenu();
            // todo проверять по номеру телефона который находится в колбак, есть ли ученик с таким номером,
            //  если есть открыть панель этого ученика
            //  реализовать логику currentStudent and current Role
            //
        }
        else {
            sendStartMenu();
        }
    }



    private void sendListRoles() throws TelegramApiException {
        user = usersRepo.findByChatId(chatId);
        assignRoles(user);

        List<Role> roles = user.getRoles();
        List<String> rolesNames = new ArrayList<>();
        List<String> rolesIds = new ArrayList<>();
        for (Role role: roles){
            rolesNames.add(role.getStrName());
            rolesIds.add(role.name());
        }
        //////////////////////////////////////////
        List<Student> students = studentRepo.findByPhone(user.getPhone());
        for (Student student : students){
            rolesNames.add("Ученик:" +student.getFullName());
            rolesIds.add(Role.ROLE_STUDENT.name()+";" + student.getId());

            student.setUser(user);
            studentRepo.save(student);

        }
        //////////////////////////////////////////


        ButtonsLeaf buttonsLeaf = new ButtonsLeaf(rolesNames, rolesIds);
        sendMessageWithKeyboard(6, buttonsLeaf.getListButtonWithDataList());

        waitingType = WaitingType.CHOOSE_ROLE;
    }

    private void assignRoles(User user) {
        Teacher teacher = teacherRepo.findByPhone(user.getPhone());
        if (teacher != null){
            teacher.setUser(user);
            teacherRepo.save(teacher);
            user.addRole(Role.ROLE_CLASSROOM_TEACHER);
            usersRepo.save(user);
        }





    }


    protected boolean isLong(String mess) {
        try {
            Long.parseLong(mess);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
