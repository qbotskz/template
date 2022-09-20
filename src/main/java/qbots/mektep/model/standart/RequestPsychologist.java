package qbots.mektep.model.standart;

import lombok.Data;
import qbots.mektep.enums.FileType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class RequestPsychologist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long     id;

    @ManyToOne
    Student sender;

    private String  text;
    private String  fileId;
    private FileType fileType;


    private String  answer;




}