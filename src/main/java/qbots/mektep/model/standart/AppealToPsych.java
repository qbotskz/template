package qbots.mektep.model.standart;

import lombok.Data;
import qbots.mektep.enums.FileType;
import qbots.mektep.enums.Role;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class AppealToPsych {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long     id;

    @ManyToOne
    Teacher teacher;

    @ManyToOne
    Student student;

    private String  text;
    private String  fileId;
    private FileType fileType;
    private String  answer;
    private Date sentDate;

    @Enumerated
    Role role;

    public String getSenderName(){
        if (getRole().equals(Role.ROLE_STUDENT)) {
            return getStudent().getFullName();
        }
        else {
            return getTeacher().getUser().getFullName();
        }
    }

    public long getSenderChatId(){
        if (getRole().equals(Role.ROLE_STUDENT)) {
            return getStudent().getUser().getChatId();
        }
        else {
            return getTeacher().getUser().getChatId();
        }
    }

}