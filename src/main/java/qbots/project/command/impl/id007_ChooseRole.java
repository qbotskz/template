package qbots.project.command.impl;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.project.command.Command;
import qbots.project.enums.Role;
import qbots.project.enums.WaitingType;

import qbots.project.model.standart.User;
import qbots.project.util.ButtonsLeaf;

import java.util.ArrayList;
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
                user = usersRepo.findByChatId(chatId);
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
            user.setChosenRole(Role.ROLE_ADMIN);
        }
        else if (updateMessageText.equals(Role.ROLE_PATIENT.name())){
            sendPatientPanel();;
            user.setChosenRole(Role.ROLE_PATIENT);
        }

        else if (updateMessageText.equals(Role.ROLE_NURSE.name())){
            sendNursePanel();;
            user.setChosenRole(Role.ROLE_NURSE);
        }

        usersRepo.save(user);
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



        ButtonsLeaf buttonsLeaf = new ButtonsLeaf(rolesNames, rolesIds);
        sendMessageWithKeyboard(6, buttonsLeaf.getListButtonWithDataList());

        waitingType = WaitingType.CHOOSE_ROLE;
    }

    private void assignRoles(User user) {


        //todo



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
