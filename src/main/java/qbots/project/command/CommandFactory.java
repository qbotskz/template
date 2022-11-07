package qbots.project.command;


import qbots.project.command.impl.*;
import qbots.project.command.impl.admin.*;
import qbots.project.exceptions.NotRealizedMethodException;

public class CommandFactory {

    public static qbots.project.command.Command getCommand(long id) {
        qbots.project.command.Command result = getCommandWithoutReflection((int) id);
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
            case 6:
                return new id006_EditAdmin();
            case 7:
                return new id007_ChooseRole();

            case 300:
                return new id300_Admin();
            case 999:
                return new id004_FileOrPhoto();

        }
        return null;
    }
}
