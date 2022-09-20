package qbots.mektep.command.impl;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.Role;
import qbots.mektep.enums.WaitingType;
import qbots.mektep.model.standart.*;
import qbots.mektep.util.ButtonsLeaf;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class id020_SocPassportParent extends Command {
    User user;
//    Classroom currentClassroom;
    Student currentStudent;
    private int delMess;
//    Teacher teacher;
    SocPassport socPassport;


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
                if (checkParent()){
                    sendStudents();
                }
                else {
                    sendMessage(165);
                    return EXIT;
                }

                if (isButton(123)){
                    sendMessageWithKeyboard(132, 13);
                }
                else if (isButton(129)){ // edit
                    sendStudents();
                }
                else if (isButton(130)){ // report
                    // todo
                }

                return COMEBACK;
            case CHOOSE_STUDENT:
                deleteUpdateMess();
                if (hasCallbackQuery()){
                    currentStudent = studentRepo.findById(getLong(updateMessageText));
                    if (currentStudent != null){
                        socPassport = socPassportRepo.findByStudent(currentStudent);
                        if (socPassport == null){
                            socPassport = new SocPassport();
                            socPassport.setStudent(currentStudent);
                        }
                        sendFIO();
                    }
                    else {
                        wrongData();
                        sendStudents();
                    }
                }
                else {
                    wrongData();
                    sendStudents();
                }
                return COMEBACK;

            case SET_FULL_NAME:
                if (hasMessageText()){
                    sendBirthDate();
                    socPassport.setFullName(updateMessageText);
                }
                else {
                    sendFIO();
                    wrongData();

                }
                return COMEBACK;
            case SET_BIRTH_DATE:
                deleteUpdateMess();
                if (isButton(6)){
                    sendFIO();
                }
                else if (isButton(7)){
                    sendIIN();
                }
                else if (hasMessageText()){
                    sendIIN();
                    socPassport.setBirthDate(updateMessageText);
                }
                return COMEBACK;
            case SET_IIN:
                deleteUpdateMess();
                if (isButton(6)){
                    sendBirthDate();
                }
                else if (isButton(7)){
                    sendPerformance();
                }
                else if (hasMessageText() && isIIN(updateMessageText)){
                    sendPerformance();
                    socPassport.setIin(updateMessageText);
                }
                else {
                    wrongData();
                    sendIIN();
                }
                return COMEBACK;
            case SET_PERFORMANCE:
                deleteUpdateMess();
                if (isButton(6)){
                    sendIIN();
                }
                else if (isButton(7)){
                    sendBehavior();
                }
                else if (hasMessageText() ){
                    sendBehavior();
                    socPassport.setPerformance(updateMessageText);
                }
                else {
                    wrongData();
                    sendPerformance();
                }
                return COMEBACK;
            case SET_BEHAVIOUR:
                deleteUpdateMess();
                if (isButton(6)){
                    sendPerformance();
                }
                else if (isButton(7)){
                    sendNationality();
                }
                else if (hasMessageText() ){
                    sendNationality();
                    socPassport.setBehaviour(updateMessageText);
                }
                else {
                    wrongData();
                    sendBehavior();
                }
                return COMEBACK;
            case SET_NATIONALITY:
                deleteUpdateMess();
                if (isButton(6)){
                    sendBehavior();
                }
                else if (isButton(7)){
                    sendFathersFIO();
                }
                else if (hasMessageText() ){
                    sendFathersFIO();
                    socPassport.setNationality(updateMessageText);
                }
                else {
                    wrongData();
                    sendNationality();
                }
                return COMEBACK;
            case SET_FATHERSFIO:
                deleteUpdateMess();
                if (isButton(6)){
                    sendNationality();
                }
                else if (isButton(7)){
                    sendFathersJob();
                }
                else if (hasMessageText() ){
                    sendFathersJob();
                    socPassport.setFatherFullName(updateMessageText);
                }
                else {
                    wrongData();
                    sendNationality();
                }
                return COMEBACK;
            case SET_FATHERS_JOB:
                deleteUpdateMess();
                if (isButton(6)){
                    sendFathersFIO();
                }
                else if (isButton(7)){
                    sendMothersFIO();
                }
                else if (hasMessageText() ){
                    sendMothersFIO();
                    socPassport.setFathersJobPlace(updateMessageText);
                }
                else {
                    wrongData();
                    sendFathersJob();
                }
                return COMEBACK;
            case SET_MOTHERS_FIO:
                deleteUpdateMess();
                if (isButton(6)){
                    sendFathersJob();
                }
                else if (isButton(7)){
                    sendMothersJob();
                }
                else if (hasMessageText() ){
                    sendMothersJob();
                    socPassport.setMotherFullName(updateMessageText);
                }
                else {
                    wrongData();
                    sendMothersFIO();
                }
                return COMEBACK;
            case SET_MOTHERS_JOB:
                deleteUpdateMess();
                if (isButton(6)){
                    sendMothersFIO();
                }
                else if (isButton(7)){
                    sendAddress();
                }
                else if (hasMessageText() ){
                    sendAddress();
                    socPassport.setMothersJobPlace(updateMessageText);
                }
                else {
                    wrongData();
                    sendMothersJob();
                }
                return COMEBACK;
            case SET_ADDRESS:
                deleteUpdateMess();
                if (isButton(6)){
                    sendMothersJob();
                }
                else if (isButton(7)){
                    sendMicroplace();
                }
                else if (hasMessageText() ){
                    sendMicroplace();
                    socPassport.setAddress(updateMessageText);
                }
                else {
                    wrongData();
                    sendAddress();
                }
                return COMEBACK;
            case SET_MICRO_PLACE:
                deleteUpdateMess();
                if (isButton(6)){
                    sendAddress();
                }
                else if (isButton(7)){
                    sendSocailStatus();
                }
                else if (hasMessageText() ){
                    sendSocailStatus();
                    socPassport.setMicroarea(updateMessageText);
                }
                else {
                    wrongData();
                    sendMicroplace();
                }
                return COMEBACK;
            case SET_SOCIAL_STATUS:
                deleteUpdateMess();
                if (isButton(6)){
                    sendMicroplace();
                }
                else if (isButton(7)){
                    sendOthersInFam();
                }
                else if (hasMessageText() ){
                    sendOthersInFam();
                    socPassport.setSocStatus(updateMessageText);
                }
                else {
                    wrongData();
                    sendSocailStatus();
                }
                return COMEBACK;
            case SET_OTHERS_IN_FAM:
                deleteUpdateMess();
                if (isButton(6)){
                    sendSocailStatus();
                }
                else if (isButton(7)){
                    sendClubs();
                }
                else if (hasMessageText() ){
                    sendClubs();
                    socPassport.setOthersInFamily(updateMessageText);
                }
                else {
                    wrongData();
                    sendOthersInFam();
                }
                return COMEBACK;
            case SET_CLUBS:
                deleteUpdateMess();
                if (isButton(6)){
                    sendOthersInFam();
                }
                else if (isButton(7)){
                    sendEmail();
                }
                else if (hasMessageText() ){
                    sendEmail();
                    socPassport.setClubs(updateMessageText);
                }
                else {
                    wrongData();
                    sendClubs();
                }
                return COMEBACK;
            case SET_EMAIL:
                deleteUpdateMess();
                if (isButton(6)){
                    sendClubs();
                }
                else if (isButton(7)){
                    sendPhoneNumber();
                }
                else if (hasMessageText() ){
                    sendPhoneNumber();
                    socPassport.setEmail(updateMessageText);
                }
                else {
                    wrongData();
                    sendEmail();
                }
                return COMEBACK;
            case SET_PHONE_NUMBER:
                deleteUpdateMess();
                if (isButton(6)){
                    sendEmail();
                }
                else if (isButton(7)){
                    sendSuccess();
                    socPassportRepo.save(socPassport);
                    return EXIT;
                }
                else if (hasMessageText() ) {
                    socPassport.setPhone(updateMessageText);
                    socPassportRepo.save(socPassport);
                    sendSuccess();
                    return EXIT;
                }
                else {
                    wrongData();
                    sendEmail();
                }
                return COMEBACK;

        }

        return COMEBACK;
    }

    private void sendPhoneNumber() throws TelegramApiException {
        deleteMessage(delMess);
        String str = getText(151)+next+getText(135) +
                getValue(socPassport.getPhone());
        delMess = sendMessageWithKeyboard(str,11);
        waitingType = WaitingType.SET_PHONE_NUMBER;
    }

    private void sendEmail() throws TelegramApiException {
        deleteMessage(delMess);
        String str = getText(150)+next+getText(135) +
                getValue(socPassport.getEmail());
        delMess = sendMessageWithKeyboard(str,11);
        waitingType = WaitingType.SET_EMAIL;
    }

    private void sendClubs() throws TelegramApiException {
        deleteMessage(delMess);
        String str = getText(149)+next+getText(135) +
                getValue(socPassport.getClubs());
        delMess = sendMessageWithKeyboard(str,11);
        waitingType = WaitingType.SET_CLUBS;
    }

    private void sendOthersInFam() throws TelegramApiException {
        deleteMessage(delMess);
        String str = getText(148)+next+getText(135) +
                getValue(socPassport.getOthersInFamily());
        delMess = sendMessageWithKeyboard(str,11);
        waitingType = WaitingType.SET_OTHERS_IN_FAM;
    }

    private void sendSocailStatus() throws TelegramApiException {
        deleteMessage(delMess);
        String str = getText(147)+next+getText(135) +
                getValue(socPassport.getSocStatus());
        delMess = sendMessageWithKeyboard(str,11);
        waitingType = WaitingType.SET_SOCIAL_STATUS;
    }

    private void sendMicroplace() throws TelegramApiException {
        deleteMessage(delMess);
        String str = getText(146)+next+getText(135) +
                getValue(socPassport.getMicroarea());
        delMess = sendMessageWithKeyboard(str,11);
        waitingType = WaitingType.SET_MICRO_PLACE;
    }

    private void sendAddress() throws TelegramApiException {
        deleteMessage(delMess);
        String str = getText(145)+next+getText(135) +
                getValue(socPassport.getAddress());
        delMess = sendMessageWithKeyboard(str,11);
        waitingType = WaitingType.SET_ADDRESS;
    }

    private void sendMothersJob() throws TelegramApiException {
        deleteMessage(delMess);
        String str = getText(144)+next+getText(135) +
                getValue(socPassport.getMothersJobPlace());
        delMess = sendMessageWithKeyboard(str,11);
        waitingType = WaitingType.SET_MOTHERS_JOB;
    }

    private void sendMothersFIO() throws TelegramApiException {
        deleteMessage(delMess);
        String str = getText(143)+next+getText(135) +
                getValue(socPassport.getMotherFullName());
        delMess = sendMessageWithKeyboard(str,11);
        waitingType = WaitingType.SET_MOTHERS_FIO;
    }

    private void sendFathersJob() throws TelegramApiException {
        deleteMessage(delMess);
        String str = getText(142)+next+getText(135) +
                getValue(socPassport.getFathersJobPlace());
        delMess = sendMessageWithKeyboard(str,11);
        waitingType = WaitingType.SET_FATHERS_JOB;
    }

    private void sendFathersFIO() throws TelegramApiException {
        deleteMessage(delMess);
        String str = getText(141)+next+getText(135) +
                getValue(socPassport.getFatherFullName());
        delMess = sendMessageWithKeyboard(str,11);
        waitingType = WaitingType.SET_FATHERSFIO;
    }

    private void sendNationality() throws TelegramApiException {
        deleteMessage(delMess);
        String str = getText(140)+next+getText(135) +
                getValue(socPassport.getNationality());
        delMess = sendMessageWithKeyboard(str,11);
        waitingType = WaitingType.SET_NATIONALITY;
    }

    private void sendBehavior() throws TelegramApiException {
        deleteMessage(delMess);
        String str = getText(139)+next+getText(135) +
                getValue(socPassport.getBehaviour());
        delMess = sendMessageWithKeyboard(str,11);
        waitingType = WaitingType.SET_BEHAVIOUR;
    }

    private void sendPerformance() throws TelegramApiException {
        deleteMessage(delMess);
        String str = getText(138)+next+getText(135) +
                getValue(socPassport.getPerformance());
        delMess = sendMessageWithKeyboard(str,11);
        waitingType = WaitingType.SET_PERFORMANCE;
    }

    private void sendIIN() throws TelegramApiException {
        deleteMessage(delMess);
        String str = getText(137)+next+getText(135) +
                getValue(socPassport.getIin());
        delMess = sendMessageWithKeyboard(str,11);
        waitingType = WaitingType.SET_IIN;
    }

    private void sendBirthDate() throws TelegramApiException {
        deleteMessage(delMess);
        String str = getText(134)+next+getText(135) +
                getValue(socPassport.getBirthDate());
        delMess = sendMessageWithKeyboard(str,11);
        waitingType = WaitingType.SET_BIRTH_DATE;
    }

    private String getValue(String value) {
        if (value != null)
            return value;
        return getText(136);
    }

    private void sendFIO() throws TelegramApiException {
        deleteMessage(delMess);
        delMess = sendMessage(133);
        waitingType = WaitingType.SET_FULL_NAME;
    }

    private void sendPhoto() throws TelegramApiException {
        deleteMessage(delMess);
        delMess = sendMessage(131);
        waitingType = WaitingType.SET_PHOTO;
    }






    private void sendSuccess() throws TelegramApiException {
        deleteMessage(delMess);
        sendMessage(152);
    }



    private void sendPVF() throws TelegramApiException {
        deleteMessage(delMess);
        delMess = sendMessageWithKeyboard(123, 11);
        waitingType = WaitingType.SET_PHOTO_VIDEO_FILE;
    }

    private void sendMailingText() throws TelegramApiException {
        deleteMessage(delMess);
        delMess = sendMessageWithKeyboard(122, 10);
        waitingType = WaitingType.SET_TEXT;
    }

    private void sendStudents() throws TelegramApiException {
        deleteMessage(delMess);
        ButtonsLeaf buttonsLeaf = new ButtonsLeaf(getNames(studentRepo.findByParent(user)), getIds(studentRepo.findByParent(user)));
        delMess = sendMessageWithKeyboard(129, buttonsLeaf.getListButtonWithDataList());
        waitingType = WaitingType.CHOOSE_STUDENT;
    }

    private List<String> getIds(List<Student> students) {
        List<String> names = new ArrayList<>();
        for (Student student : students){
            names.add(String.valueOf(student.getId()));
        }
        return names;
    }

    private List<String> getNames(List<Student> students) {
        List<String> names = new ArrayList<>();
        for (Student student : students){
            names.add(student.getFullName());
        }
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
