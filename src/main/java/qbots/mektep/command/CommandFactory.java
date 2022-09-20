package qbots.mektep.command;


import qbots.mektep.command.impl.*;
import qbots.mektep.command.impl.admin.*;
import qbots.mektep.command.impl.classroomTeacher.*;
import qbots.mektep.command.impl.psychologist.id600_ConductTest;
import qbots.mektep.command.impl.psychologist.id601_TestReport;
import qbots.mektep.command.impl.psychologist.id602_MailingPsych;
import qbots.mektep.command.impl.psychologist.id603_Appeals;
import qbots.mektep.command.impl.student.*;
import qbots.mektep.exceptions.NotRealizedMethodException;

public class CommandFactory {

    public static qbots.mektep.command.Command getCommand(long id) {
        qbots.mektep.command.Command result = getCommandWithoutReflection((int) id);
        if (result == null) throw new NotRealizedMethodException("Not realized for type: " + id);
        return result;
    }
    private static Command getCommandWithoutReflection(int id) {
        switch (id) {
            case 1:
                return new id001_ShowInfo();
            case 2:
                return new id002_Registration();
            case 3:
                return new id003_SelectionLanguage();
            case 4:
                return new id004_RegistrationTeacher();
            case 6:
                return new id006_EditAdmin();
            case 5:
                return new id005_EditClassroomTeacher();
            case 7:
                return new id007_ChooseRole();
            case 8:
                return new id008_MailingClassTeacher();

            case 10:
                return new id010_MyClass();

            case 11:
                return new id011_AddClass();
            case 12:
                return new id012_SocPassportClassroomTeacher();
            case 13:
                return new id013_StudentsStatistics();
            case 14:
                return new id014_ChatsClassroomTeacher();
            case 15:
                return new id015_FluorographyClassroomTeacher();
            case 16:
                return new id016_ReferencesStudents();
            case 19:
                return new id019_FluorographyParent();
            case 20:
                return new id020_SocPassportParent();
            case 21:
                return new id021_Achivement();
            case 22:
                return new id022_FluorographyStudent();
            case 23:
                return new id23_OnayStudent();
            case 24:
                return new id024_AppealFromStudent();
            case 25:
                return new id025_MyAchievementsStudent();
            case 26:
                return new id026_ChatsStudent();
            case 27:
                return new id027_ChatsStudent();
            case 28:
                return new id028_PassTheTest();
            case 29:
                return new id029_AppealsHistory();
            case 30:
                return new id030_AppealFromTeacher();
            case 31:
                return new id031_AppealsHistoryTeacher();
            case 32:
                return new id032_MyTasks();
            case 200:
                return new id200_Onay();
            case 201:
                return new id201_ReqFromParent();
            case 202:
                return new id202_AddChild();
            case 300:
                return new id300_Admin();
            case 301:
                return new id301_EditSurvey();
//            case 302:
//                return new id302_AddTest();
            case 600:
                return new id600_ConductTest();
            case 601:
                return new id601_TestReport();
            case 602:
                return new id602_MailingPsych();
            case 603:
                return new id603_Appeals();
            case 999:
                return new id004_FileOrPhoto();

        }
        return null;
    }
}
