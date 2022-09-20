package qbots.mektep.model.standart;

import lombok.Data;
import qbots.mektep.enums.FileType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class IndividualTask {

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





    private String  answerText;
    private String  answerFileId;
    private FileType answerFileType;

    private Date sentDate;

}