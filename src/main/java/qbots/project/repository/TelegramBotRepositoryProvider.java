package qbots.project.repository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TelegramBotRepositoryProvider {

    @Getter
    @Setter
    private static PropertiesRepo propertiesRepo;
    
    @Getter
    @Setter
    private static UsersRepo usersRepo;
    @Getter
    @Setter
    private static ButtonRepo buttonRepo;
    @Getter
    @Setter
    private static MessageRepo messageRepo;
    @Getter
    @Setter
    private static KeyboardRepo keyboardRepo;








    //---------------------------------------------------------------
    @Autowired
    public TelegramBotRepositoryProvider(
            PropertiesRepo propertiesRepo,
            UsersRepo usersRepo, ButtonRepo buttonRepo, MessageRepo messageRepo,
            KeyboardRepo keyboardRepo

    ) {
        setPropertiesRepo(propertiesRepo);
        setUsersRepo(usersRepo);
        setButtonRepo(buttonRepo);
        setMessageRepo(messageRepo);
        setKeyboardRepo(keyboardRepo);
    }
}
