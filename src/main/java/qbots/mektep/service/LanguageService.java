package qbots.mektep.service;

import org.springframework.stereotype.Component;
import qbots.mektep.enums.Language;
import qbots.mektep.model.standart.User;
//import qbots.mektep.repository.LanguageUserRepo;
import qbots.mektep.repository.TelegramBotRepositoryProvider;
import qbots.mektep.repository.UsersRepo;



@Component
public class LanguageService {

//    private static  Map<Long, Language>     languageMap         = new HashMap<>();
//    private static LanguageUserRepo languageUserRepo    = TelegramBotRepositoryProvider.getLanguageUserRepo();
    private static UsersRepo usersRepo           = TelegramBotRepositoryProvider.getUsersRepo();

    public  static  Language    getLanguage(long chatId) {
        User user = usersRepo.findByChatId(chatId);
        if (user != null && user.getLanguage() != null){
            return user.getLanguage();
        }

        else {
            return Language.ru;
        }

//        Language language = languageMap.get(chatId);
//        if (language == null) {
//            LanguageUser languageUser = languageUserRepo.getByChatId(chatId);
//            if (languageUser != null) {
//                language = Language.getById(languageUser.getLanguageId());
//                languageMap.put(chatId, language);
//            }
//        }
//        return language;
    }

    public  static  void        setLanguage(long chatId, Language language) {
        User user = usersRepo.findByChatId(chatId);
        if (user == null) {
            user = new User();
            user.setChatId(chatId);
        }
        user.setLanguage(language);
        usersRepo.save(user);


//        languageMap.put(chatId, language);
//        LanguageUser languageUser = languageUserRepo.getByChatId(chatId);
//        if (languageUser == null) {
//            languageUserRepo.save(new LanguageUser().setChatId(chatId).setLanguageId(language.getId()));
//        } else {
//            languageUserRepo.save(languageUser.setLanguageId(language.getId()));
//        }
    }
}
