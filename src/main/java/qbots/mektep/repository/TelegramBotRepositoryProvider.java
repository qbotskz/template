package qbots.mektep.repository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qbots.mektep.repository.studentTest.*;


@Component
public class TelegramBotRepositoryProvider {

    @Getter
    @Setter
    private static PropertiesRepo propertiesRepo;
//    @Getter
//    @Setter
//    private static LanguageUserRepo languageUserRepo;
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

    @Getter
    @Setter
    private static ClassroomRepo classroomRepo;
;
    @Getter
    @Setter
    private static RegistrationTeacherRepo registrationTeacherRepo;
;
    @Getter
    @Setter
    private static TeacherRepo teacherRepo;

;
    @Getter
    @Setter
    private static StudentRepo studentRepo;
    @Getter
    @Setter
    private static SocPassportRepo socPassportRepo;

    @Getter
    @Setter
    private static ReferencesStudentsRepo referencesStudentsRepo;


    @Getter
    @Setter
    private static RequestFromParentRepo requestFromParentRepo;


    @Getter
    @Setter
    private static OnayRepo onayRepo;


    @Getter
    @Setter
    private static AppealFromStudentRepo appealFromStudentRepo;

    @Getter
    @Setter
    private static StudentsAchievementsRepo studentsAchievementsRepo;

    @Getter
    @Setter
    private static TestRepo testRepo;

    @Getter
    @Setter
    private static QuestionRepo questionRepo;

    @Getter
    @Setter
    private static VariantRepo variantRepo;

    @Getter
    @Setter
    private static StudentsAnswerRepo studentsAnswerRepo;

    @Getter
    @Setter
    private static IndividualTaskRepo individualTaskRepo;





    //---------------------------------------------------------------
    @Autowired
    public TelegramBotRepositoryProvider(
            PropertiesRepo propertiesRepo,RegistrationTeacherRepo registrationTeacherRepo,
            UsersRepo usersRepo, ButtonRepo buttonRepo, MessageRepo messageRepo,
            KeyboardRepo keyboardRepo,
            ClassroomRepo classroomRepo,StudentRepo studentRepo,
            TeacherRepo teacherRepo,SocPassportRepo socPassportRepo,
            ReferencesStudentsRepo referencesStudentsRepo,
            RequestFromParentRepo requestFromParentRepo,
            OnayRepo onayRepo,AppealFromStudentRepo appealFromStudentRepo,
            StudentsAchievementsRepo studentsAchievementsRepo,TestRepo testRepo,
            QuestionRepo questionRepo,VariantRepo variantRepo,
            StudentsAnswerRepo studentsAnswerRepo,IndividualTaskRepo individualTaskRepo
    ) {
        setPropertiesRepo(propertiesRepo);
        setUsersRepo(usersRepo);
        setButtonRepo(buttonRepo);
        setMessageRepo(messageRepo);
        setKeyboardRepo(keyboardRepo);
        setRegistrationTeacherRepo(registrationTeacherRepo);
        setClassroomRepo(classroomRepo);
        setStudentRepo(studentRepo);
        setTeacherRepo(teacherRepo);
        setSocPassportRepo(socPassportRepo);
        setReferencesStudentsRepo(referencesStudentsRepo);
        setRequestFromParentRepo(requestFromParentRepo);
        setOnayRepo(onayRepo);
        setAppealFromStudentRepo(appealFromStudentRepo);
        setStudentsAchievementsRepo(studentsAchievementsRepo);
        setTestRepo(testRepo);
        setQuestionRepo(questionRepo);
        setVariantRepo(variantRepo);
        setStudentsAnswerRepo(studentsAnswerRepo);
        setIndividualTaskRepo(individualTaskRepo);
    }
}
