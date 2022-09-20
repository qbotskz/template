package qbots.mektep.command.impl.classroomTeacher;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.WaitingType;
import qbots.mektep.model.standart.RegistrationTeacher;

import static jdk.nashorn.internal.runtime.JSType.isNumber;


public class id004_RegistrationTeacher extends Command {

    RegistrationTeacher registrationTeacher;
    int delMes;

    @Override
    public boolean execute() throws TelegramApiException {

        if (!isRegistered()){
            sendChooseLanguage();
            return EXIT;
        }

        switch (waitingType){
            case START:
                deleteUpdateMess();
                registrationTeacher = registrationTeacherRepo.findByUserChatId(chatId);
                if (registrationTeacher == null){
                    registrationTeacher = new RegistrationTeacher();
                    // todo
                }
//                else {
//                    sendFIO();
//                    registrationTeacher = new RegistrationTeacher();
//                    registrationTeacher.setUser(usersRepo.findByChatId(chatId));
//                }
                sendFIO();
                return COMEBACK;
            case SET_FULL_NAME:
                deleteUpdateMess();
                if (hasMessageText()){
                    sendIIN();
                    registrationTeacher.setFullName(updateMessageText);
                }
                else {
                    wrongData();
                    sendFIO();
                }
                return COMEBACK;
            case SET_IIN:
                deleteUpdateMess();
                if (hasMessageText() && isIIN(updateMessageText)){
                    sendVUZ();
                    registrationTeacher.setIin(updateMessageText);
                }
                else {
                    wrongData();
                    sendIIN();
                }
                return COMEBACK;
            case SET_EDUCATION:
                deleteUpdateMess();
                if (hasMessageText()){
                    sendDiplomaNumber();
                    registrationTeacher.setEducation(updateMessageText);
                }
                else {
                    wrongData();
                    sendVUZ();
                }
                return COMEBACK;
            case SET_DIPLOMA_NUMBER:
                deleteUpdateMess();
                if (hasMessageText()){
                    sendScanDiploma();
                    registrationTeacher.setDiplomaNumber(updateMessageText);
                }
                else {
                    wrongData();
                    sendDiplomaNumber();
                }
                return COMEBACK;
            case SET_DIPLOMA_SCAN:
                deleteUpdateMess();
                if (hasPhoto()){
                    sendSubject();
                    registrationTeacher.setDiplomaScanPhoto(updateMessagePhoto);
                }
                else if (hasDocument()){
                    sendSubject();
                    registrationTeacher.setDiplomaScanDoc(update.getMessage().getDocument().getFileId());
                }
                else {
                    wrongData();
                    sendScanDiploma();
                }
                return COMEBACK;
            case SET_SUBJECT:
                deleteUpdateMess();
                if (hasMessageText()){
                    sendExperience();
                    registrationTeacher.setSubject(updateMessageText);
                }
                else {
                    wrongData();
                    sendSubject();
                }
                return COMEBACK;
            case SET_EXPERIENCE:
                deleteUpdateMess();
                if (hasMessageText() && isNumberr(updateMessageText)){
                    sendYearInSchool();
                    registrationTeacher.setExperience(Integer.parseInt(updateMessageText));
                }
                else {
                    wrongData();
                    sendExperience();
                }
                return COMEBACK;
            case SET_YEAR_IN_SCHOOL:
                deleteUpdateMess();
                if (hasMessageText() && isNumberr(updateMessageText)){
                    sendCategory1();
                    registrationTeacher.setExperience(Integer.parseInt(updateMessageText));
                }
                else {
                    wrongData();
                    sendYearInSchool();
                }
                return COMEBACK;
            case SET_CATEGORY1:
                deleteUpdateMess();
                if (isButton(101) ||isButton(102) ||isButton(103)){
                    sendCategory2();
                    registrationTeacher.setCategory(updateMessageText);
                }
                else if (isButton(104)){
                    sendQual();
                    registrationTeacher.setCategory(updateMessageText);
                }
                else {
                    wrongData();
                    sendCategory1();
                }
                return COMEBACK;
            case SET_CATEGORY2:
                deleteUpdateMess();
                if (isButton(105) || isButton(106) || isButton(107) || isButton(108)){
                    sendQual();
                    registrationTeacher.setCategory2(updateMessageText);
                }
                else {
                    wrongData();
                    sendCategory2();
                }
                return COMEBACK;
            case SET_QUALIFACATION:
                deleteUpdateMess();
                if (hasMessageText()){
                    sendAchievements();
                    registrationTeacher.setQualification(updateMessageText);
                }
                else {
                    wrongData();
                    sendQual();
                }
                return COMEBACK;
            case SET_ACHIEVEMENTS:
                deleteUpdateMess();
                if (hasMessageText()){
                    sendPlace();
                    registrationTeacher.setAchievements(updateMessageText);
                }
                else {
                    wrongData();
                    sendAchievements();
                }
                return COMEBACK;
            case SET_PLACES:
                deleteUpdateMess();
                if (isButton(109) ||isButton(110) ||isButton(111) ||isButton(112) ||isButton(113) ){
                    sendLevel();
                    registrationTeacher.setPlace(updateMessageText);
                }
                else {
                    wrongData();
                    sendPlace();
                }
                return COMEBACK;
            case SET_LEVEL:
                deleteUpdateMess();
                if (isButton(114) || isButton(115) || isButton(116) || isButton(117) || isButton(118)){
                    sendAwards();
                    registrationTeacher.setLevel(updateMessageText);
                }
                else {
                    wrongData();
                    sendLevel();
                }
                return COMEBACK;
            case SET_AWARDS:
                deleteUpdateMess();
                if (hasMessageText()){
                    sendAddress();
                    registrationTeacher.setAwards(updateMessageText);
                }
                else {
                    wrongData();
                    sendAwards();
                }
                return COMEBACK;
            case SET_ADDRESS:
                deleteUpdateMess();
                if (hasMessageText()){
                    sendPhoneNumber();
                    registrationTeacher.setAddress(updateMessageText);
                }
                else {
                    wrongData();
                    sendAddress();
                }
                return COMEBACK;
            case SET_PHONE_NUMBER:
                deleteUpdateMess();
                if (hasMessageText()){
                    registrationTeacher.setPhoneNumber(updateMessageText);
                    registrationTeacherRepo.save(registrationTeacher);
                    sendFinish();
                    return EXIT;
                }
                else {
                    wrongData();
                    sendPhoneNumber();
                }
                return COMEBACK;
        }
        return EXIT;
    }

    private void sendFinish() throws TelegramApiException {
        delete(delMes);
        delMes = sendMessage(118);
    }

    private void sendPhoneNumber() throws TelegramApiException {
        delete(delMes);
        delMes = sendMessage(117);
        waitingType = WaitingType.SET_PHONE_NUMBER;
    }


    private void sendAddress() throws TelegramApiException {
        delete(delMes);
        delMes = sendMessage(116);
        waitingType = WaitingType.SET_ADDRESS;
    }

    private void sendAwards() throws TelegramApiException {
        delete(delMes);
        delMes = sendMessage(115);
        waitingType = WaitingType.SET_AWARDS;
    }

    private void sendLevel() throws TelegramApiException {
        delete(delMes);
        delMes = sendMessageWithKeyboard(114,6);
        waitingType = WaitingType.SET_LEVEL;
    }


    private void sendPlace() throws TelegramApiException {
        delete(delMes);
        delMes = sendMessageWithKeyboard(113,5);
        waitingType = WaitingType.SET_PLACES;
    }

    private void sendAchievements() throws TelegramApiException {
        delete(delMes);
        delMes = sendMessage(112);
        waitingType  = WaitingType.SET_ACHIEVEMENTS;
    }

    private void sendQual() throws TelegramApiException {
        delete(delMes);
        delMes = sendMessage(111);
        waitingType  = WaitingType.SET_QUALIFACATION;
    }

    private void sendCategory2() throws TelegramApiException {
        delete(delMes);
        delMes = sendMessageWithKeyboard(110,4);
        waitingType = WaitingType.SET_CATEGORY2;
    }

    private void sendCategory1() throws TelegramApiException {
        delete(delMes);
        delMes = sendMessageWithKeyboard(109,3);
        waitingType = WaitingType.SET_CATEGORY1;
    }

    private void sendYearInSchool() throws TelegramApiException {
        delete(delMes);
        delMes = sendMessage(108);
        waitingType = WaitingType.SET_YEAR_IN_SCHOOL;
    }

    private void sendExperience() throws TelegramApiException {
        delete(delMes);
        delMes = sendMessage(107);
        waitingType = WaitingType.SET_EXPERIENCE;
    }

    private void sendSubject() throws TelegramApiException {
        delete(delMes);
        delMes = sendMessage(106);
        waitingType = WaitingType.SET_SUBJECT;
    }

    private void sendScanDiploma() throws TelegramApiException {
        delete(delMes);
        delMes = sendMessage(105);
        waitingType = WaitingType.SET_DIPLOMA_SCAN;
    }

    private boolean isNumberr(String updateMessageText) {
        try {
            Long.parseLong(updateMessageText);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private void sendDiplomaNumber() throws TelegramApiException {
        delete(delMes);
        delMes = sendMessage(104);
        waitingType = WaitingType.SET_DIPLOMA_NUMBER;
    }

    private void sendVUZ() throws TelegramApiException {
        delete(delMes);
        delMes = sendMessage(103);
        waitingType = WaitingType.SET_EDUCATION;
    }



    private void sendIIN() throws TelegramApiException {
        delete(delMes);
        delMes = sendMessage(102);
        waitingType = WaitingType.SET_IIN;
    }

    private void sendFIO() throws TelegramApiException {
        delMes = sendMessage(101);
        waitingType = WaitingType.SET_FULL_NAME;
    }

    private void wrongData() throws TelegramApiException {
        sendMessage(4);
    }
}
