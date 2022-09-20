package qbots.mektep.command.impl.admin;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.Role;
import qbots.mektep.model.standart.User;


import java.util.List;

@Slf4j
public class id006_EditAdmin extends Command {
    User user;

    private int messId;
    private int notRegisteredMessId;
    private int alreadyAdminMessId;


    @Override
    public boolean execute() throws TelegramApiException {
        deleteUpdateMess();
        deleteNotRegisteredMessId();
        deleteAlreadyAdminMessId();
        user = usersRepo.findByChatId(chatId);
        if (!isRegistered()) {
            sendChooseLanguage();
            return EXIT;
        }
        if (!isAdmin()) {
            sendChooseLanguage();
            return EXIT;
        }
        if (hasContact()) {
            String phone = update.getMessage().getContact().getPhoneNumber();

            if (phone.charAt(0) == '8') {
                phone = phone.replaceFirst("8", "+7");
            } else if (phone.charAt(0) == '7') {
                phone = phone.replaceFirst("7", "+7");
            }

            return saveAdmin(phone);
        } else if (hasMessageText() && isPhoneNumber(updateMessageText)) {
            String phone = updateMessageText;

            if (phone.charAt(0) == '8') {
                phone = phone.replaceFirst("8", "+7");
            } else if (phone.charAt(0) == '7') {
                phone = phone.replaceFirst("7", "+7");
            }

            return saveAdmin(phone);

        } else if (hasMessageText() && updateMessageText.contains("/del")) {
            if (usersRepo.findAllByRolesContains(Role.ROLE_ADMIN).size() == 1) {
                return COMEBACK;
            }
            long delAdminId = getDelAdminId(updateMessageText);
            User delAdmin = usersRepo.findById(delAdminId);

            if (delAdmin != null) {
                delAdmin.deleteRole(Role.ROLE_ADMIN);
                usersRepo.save(delAdmin);
//                adminRepo.delete(adminRepo.findById(delAdminId));
                editMessage(getListAdmins(), chatId, messId);
                return COMEBACK;
            }
        } else {
            if (messId == 0) {
                messId = sendMessage(getListAdmins());
            }
            return COMEBACK;
        }
        return COMEBACK;
    }

    private void deleteAlreadyAdminMessId() {
        if (alreadyAdminMessId != 0)
            deleteMessage(alreadyAdminMessId);
    }

    private void deleteNotRegisteredMessId() {
        if (notRegisteredMessId != 0)
            deleteMessage(notRegisteredMessId);
    }

    private int getDelAdminId(String updateMessageText) {
        try {
            return Integer.parseInt(updateMessageText.substring(4));
        } catch (Exception e) {
            return -1;
        }
    }

    private boolean isPhoneNumber(String phone) {

        if (phone.charAt(0) == '8') {
            phone = phone.replaceFirst("8", "+7");
        } else if (phone.charAt(0) == '7') {
            phone = phone.replaceFirst("7", "+7");
        }
        return phone.charAt(0) == '+' && phone.charAt(1) == '7' && phone.substring(2).length() == 10 && isLong(phone.substring(2));
    }

    private boolean saveAdmin(String phone) throws TelegramApiException {
        User newAdmin = usersRepo.findByPhone(phone);
        if (newAdmin == null) {
            deleteMessage(notRegisteredMessId);
            notRegisteredMessId = sendMessage(9);
            return COMEBACK;
        }
        if (newAdmin.hasRole(Role.ROLE_ADMIN)) {
            deleteAlreadyAdminMessId();
            alreadyAdminMessId = sendMessage(10);
            return COMEBACK;
        }
        newAdmin.addRole(Role.ROLE_ADMIN);
        usersRepo.save(newAdmin);
//        Admin admin = new Admin(usersRepo.findByPhone(phone).getChatId(), newAdmin.getFullName());
//        adminRepo.save(admin);
        editMessage(getListAdmins(), chatId, messId);
        return COMEBACK;
    }

//    private void deleteUpdateMess() {
//        deleteMessage(updateMessageId);
//    }


    protected boolean isLong(String mess) {
        try {
            Long.parseLong(mess);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String getListAdmins() {
        StringBuilder admins = new StringBuilder();
        admins.append(getText(7)).append(next).append(next);
        List<User> adminList = usersRepo.findAllByRolesContains(Role.ROLE_ADMIN);
        for (User admin : adminList) {
            try {
                admins.append(admin.getFullName()).append(" ");
                if (adminList.size() > 1)
                    admins.append("❌ /del").append(admin.getId()).append(next);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        admins.append(next).append(next).append(getText(8));
        adminList.clear();
        return admins.toString();
    }
}
