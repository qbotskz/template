package qbots.mektep.command.impl;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import qbots.mektep.command.Command;
import qbots.mektep.enums.Language;
import qbots.mektep.enums.Role;
import qbots.mektep.enums.WaitingType;
import qbots.mektep.model.standart.*;
import qbots.mektep.service.LanguageService;
import qbots.mektep.service.RegistrationService;
import qbots.mektep.util.BotUtil;
import qbots.mektep.util.ButtonsLeaf;
import qbots.mektep.util.UpdateUtil;

import java.util.*;


public class id002_Registration extends Command {
    int delMes;

    Classroom currentClassroom;
    Student currentStudent;
    User user;
    @Override
    public boolean execute() throws TelegramApiException {

        switch (waitingType) {
            case START:
                if (isButton(1)) {
                    delMes = sendLanguages();
                }
                else if (isButton(3)) { // kz
                    deleteMessage(delMes);
                    deleteMessage(updateMessageId);
                    LanguageService.setLanguage(chatId, Language.kz);


                    if (!isRegistered()) {
                        sendName();
                        return COMEBACK;

                    }
//                    if (!checkParent()){
//                        user = usersRepo.findByChatId(chatId);
//                        sendClasees();
//                        return COMEBACK;
//                    }

                    else {
                        sendStartMenu();
                        return EXIT;
                    }


                }
                else if (isButton(4)) { // ru
                    deleteMessage(delMes);

                    deleteMessage(updateMessageId);
                    LanguageService.setLanguage(chatId, Language.ru);

                    if (!isRegistered()) {
                        sendName();
                        return COMEBACK;

                    }
//                    if (!checkParent()){
//                        sendClasees();
//                        return COMEBACK;
//                    }
                    else {
                        sendStartMenu();
                        return EXIT;

                    }


                }

                return COMEBACK;
            case SET_FULL_NAME:
                deleteMessage(delMes);
                deleteMessage(updateMessageId);
                if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().length() <= 50) {
                    user.setFullName(update.getMessage().getText());
                    sendPhone();
                    waitingType = WaitingType.SET_PHONE_NUMBER;
                } else {
                    wrongData();
                    sendName();
                }
                return COMEBACK;
            case SET_PHONE_NUMBER:
                if (hasContact()) {
                    deleteMessage(delMes);
                    deleteMessage(updateMessageId);
                    String phone = update.getMessage().getContact().getPhoneNumber();
                    if (update.getMessage().getContact().getPhoneNumber().startsWith("8")) {
                        phone = update.getMessage().getContact().getPhoneNumber().replaceFirst("8", "+7");
                    }
                    if (update.getMessage().getContact().getPhoneNumber().startsWith("7")) {
                        phone = update.getMessage().getContact().getPhoneNumber().replaceFirst("7", "+7");
                    }
                    user.setPhone(phone);
                    user.setUsername(UpdateUtil.getFrom(update));
                    user.addRole(Role.ROLE_PARENT);
                    user = usersRepo.save(user);

//                    sendClasees();

                    sendStartMenu();
                    return COMEBACK;
                } else {
                    wrongData();
                    sendPhone();
                    return COMEBACK;
                }

        }
        return EXIT;

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
        ButtonsLeaf buttonsLeaf = new ButtonsLeaf(getNames2(currentClassroom.getStudents()), getIds2(currentClassroom.getStudents()));
        delMes = sendMessageWithKeyboard(129, buttonsLeaf.getListButtonWithDataList());
        waitingType = WaitingType.CHOOSE_STUDENT;
    }

    private void sendClasees2() throws TelegramApiException {
        deleteMessage( delMes);
        List<Classroom > classrooms = classroomRepo.findAllByNumber(Integer.parseInt(updateMessageText));

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
    protected long getLong(String mess) {
        try {
            return Long.parseLong(mess);
        } catch (Exception e) {
            return -1;
        }
    }
    private int sendLanguages() throws TelegramApiException {
        return sendMessageWithKeyboard(5,2);
    }

    private void wrongData() throws TelegramApiException {
        sendMessage(4);
    }

    private void sendPhone() throws TelegramApiException {

        delMes = sendMessageWithKeyboard(3, getContactKeyboard());
    }

    private ReplyKeyboard getContactKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();

        Button buttonFromDb = buttonRepo.findByIdAndLangId(2, getLanguage().getId());
        KeyboardButton button = new KeyboardButton();
        String buttonText = buttonFromDb.getName();
        button.setText(buttonText);
        button.setRequestContact(true);
        keyboardRow.add(button);

        keyboardRowList.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    private int sendName() throws TelegramApiException {
        delMes=sendMessage(2);
        waitingType = WaitingType.SET_FULL_NAME;

        user = usersRepo.findByChatId(chatId);
        if (user == null){
            user = new User();
            user.setChatId(chatId);
        }

        return delMes;
    }
}
